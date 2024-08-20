package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.BookingDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.BookingHistory;
import com.uit.hotelmanagement.entities.Customer;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.exceptions.BookingStatusException;
import com.uit.hotelmanagement.exceptions.CustomLocalDateException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.exceptions.RoomNotAvailableException;
import com.uit.hotelmanagement.repositories.BookingHistoryRepository;
import com.uit.hotelmanagement.repositories.BookingRepository;
import com.uit.hotelmanagement.repositories.CustomerRepository;
import com.uit.hotelmanagement.repositories.RoomRepository;
import com.uit.hotelmanagement.utils.BookingStatus;
import com.uit.hotelmanagement.utils.HistoryType;
import com.uit.hotelmanagement.utils.RoomStatus;
import com.uit.hotelmanagement.utils.UtilityMethods;
import com.uit.hotelmanagement.utils.records.ChangeRoomRequest;
import com.uit.hotelmanagement.utils.records.CheckInRequest;
import com.uit.hotelmanagement.utils.records.CheckOutOrExtendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingHistoryRepository bookingHistoryRepository;
    private final ServiceUsageService sus;
    private BookingStatus setBookingStatus(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 4) {
            throw new ResourceNotFoundException("Booking status", "bookingStatus", index);
        }

        return switch (Integer.parseInt(index)) {
            case 1 -> BookingStatus.CONFIRMED;
            case 2 -> BookingStatus.CHECKED_IN;
            case 3 -> BookingStatus.CHECKED_OUT;
            default -> BookingStatus.CANCELLED;
        };
    }

// Post
    @Override
    public BookingDTO reserveRoom(Integer customerId, Integer roomId, BookingDTO bookingDTO) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", Integer.toString(customerId)));

        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        // check checkInDate before checkOutDate
        UtilityMethods.check_inIsBeforeCheck_out(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // kiem tra chong cheo
        if (!isRoomAvailable(existingRoom, UtilityMethods.setLocalDate(bookingDTO.getCheckInDate()), UtilityMethods.setLocalDate(bookingDTO.getCheckOutDate()))) {
            throw new RoomNotAvailableException("Room " + existingRoom.getRoomNumber() + " is not available for the specified dates.");
        }

        // change the RoomStatus of the room booked by the customer
        List<Booking> checkInBookings = bookingRepository.findByRoomAndBookingStatus(existingRoom, BookingStatus.CHECKED_IN);
        if (checkInBookings.isEmpty()) {
            existingRoom.setRoomStatus(RoomStatus.RESERVED);
        } else {
            existingRoom.setRoomStatus(RoomStatus.OCCUPIED);
        }

        // set BookingStatus as confirmed
        Booking booking = Booking.builder()
                .checkInDate(UtilityMethods.setLocalDate(bookingDTO.getCheckInDate()))
                .checkOutDate(UtilityMethods.setLocalDate(bookingDTO.getCheckOutDate()))
                .bookingStatus(BookingStatus.CONFIRMED)
                .bookingVoucher(new BigDecimal(bookingDTO.getBookingVoucher()))
                .customer(existingCustomer)
                .room(existingRoom)
                .build();

        roomRepository.save(existingRoom);
        Booking savedBooking = bookingRepository.save(booking);

        return bookingToDTO(savedBooking);
    }

// Get
    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::bookingToDTO).collect(Collectors.toList());
    }

    @Override
    public BookingDTO getBookingById(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));
        return bookingToDTO(existingBooking);
    }

    @Override
    public List<BookingDTO> getAllBookingsByCheckInDate(String checkInDate) {
        return bookingRepository.findByCheckInDate(UtilityMethods.setLocalDate(checkInDate))
                .stream().map(this::bookingToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookingsByCheckOutDate(String checkOutDate) {
        return bookingRepository.findByCheckOutDate(UtilityMethods.setLocalDate(checkOutDate))
                .stream().map(this::bookingToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookingsByBookingStatus(String bookingStatusIndex) {
        return bookingRepository.findByBookingStatus(setBookingStatus(bookingStatusIndex))
                .stream().map(this::bookingToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookingsByCustomer(Integer customerId) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", Integer.toString(customerId)));

        return bookingRepository.findByCustomer(existingCustomer)
                .stream().map(this::bookingToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookingsByRoom(Integer roomId) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        return bookingRepository.findByRoom(existingRoom)
                .stream().map(this::bookingToDTO).collect(Collectors.toList());
    }
    @Override
    public BigDecimal finalTotalPrice(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));
//        -List bhs : Lấy tất cả history của 1 booking có historyType = CHANGE_ROOM, có changeDate >= checkInDate và sắp xếp theo changeDate
//        -nếu bhs rỗng -> tính tiền như bth
//        -nếu != rỗng
//                -checkInBh: lấy ra record bookingHistory có historyType = CHECK_IN (quy định mỗi booking chỉ có duy nhất 1 record có historyType này)
//                +khởi tạo biến currentDay = check-in date
//                +khởi tạo biến currentRoom = checkInBh.getRoom() là room lúc check-in
//                +khởi tạo biến total = 0
//                +lăp qua list history:
//        1.	Lấy giá phòng
//        2.	Tính số ngày ở between currentDate - changeDate
//        3.	* and + dồn total
//        4.	Gán lại currentDate = changeDate
//        5.	Gán lại currentRoom = bh.getRoom() là phòng sẽ đổi đến ở tiếp theo
//        6.	Tiếp tục lặp
//                =>Như vậy lần gán currentRoom cuối là room cuối cùng sẽ ở cho đến khi check-out
//                +Tính tiền từ ngày đổi lần cuối đến ngày check-out
//        1.	Tính số ngày ở between currentDate – checkOutDate
//        2.	* and + dồn total


        // get all bookingHistory whose changeDate field is not null
        List<BookingHistory> bhs = bookingHistoryRepository.findByBookingWithHistoryTypeAndChangeDate(
                existingBooking, HistoryType.CHANGE_ROOM, existingBooking.getCheckInDate()
        );

        // if there are no room changes, we will charge as usual
        if (bhs.isEmpty()) {
            return existingBooking.notChangeRoomCalculate();
        }

        // get bookingHistory with historyType = CHECK_IN
        BookingHistory checkInBh = bookingHistoryRepository.findFirstByBookingAndHistoryType
                        (existingBooking, HistoryType.CHECK_IN)
                .orElseThrow(() -> new BookingStatusException("This booking has not been checked-in!"));

        LocalDate currentDate = existingBooking.getCheckInDate();
        Room currentRoom = checkInBh.getRoom();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BookingHistory bh : bhs) {
            BigDecimal roomPrice = currentRoom.getPrice();

            long daysInRoom = ChronoUnit.DAYS.between(currentDate, bh.getChangeDate());

            totalPrice = totalPrice.add(roomPrice.multiply(BigDecimal.valueOf(daysInRoom)));
            // BigDecimal là một lớp bất biến (immutable)
            // nghĩa là + - * không thay đổi đối tượng gốc mà trả về một đối tượng BigDecimal mới

            currentDate = bh.getChangeDate();
            currentRoom = bh.getRoom();
        }

        // charge for the final period up to the check-out date
        long remainingDays = ChronoUnit.DAYS.between(currentDate, existingBooking.getCheckOutDate());
        totalPrice = totalPrice.add(currentRoom.getPrice().multiply(BigDecimal.valueOf(remainingDays)));

        BigDecimal voucher = BigDecimal.ONE.subtract(existingBooking.getBookingVoucher());

        return totalPrice.multiply(voucher).setScale(2, RoundingMode.HALF_EVEN);
    }

// Put
    @Override
    public BookingDTO updateBooking(Integer bookingId, Integer roomId, BookingDTO bookingDTO) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        // check-in date must be before check-out date
        UtilityMethods.check_inIsBeforeCheck_out(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // update existingBooking
        existingBooking.setCheckInDate(UtilityMethods.setLocalDate(bookingDTO.getCheckInDate()));
        existingBooking.setCheckOutDate(UtilityMethods.setLocalDate(bookingDTO.getCheckOutDate()));
        existingBooking.setBookingVoucher(new BigDecimal(bookingDTO.getBookingVoucher()));
        existingBooking.setRoom(existingRoom);

        // save
        Booking updatedBooking = bookingRepository.save(existingBooking);

        return bookingToDTO(updatedBooking);
    }

    @Override
    public BookingDTO checkIn(Integer bookingId, CheckInRequest checkInRequest) {
        // check for an existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        // check-in date before check-out date
        UtilityMethods.check_inIsBeforeCheck_out(checkInRequest.actualCheckInDate(), checkInRequest.actualCheckOutDate());

        // change BookingStatus to CHECKED_IN
        if (existingBooking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BookingStatusException("This booking has not been confirmed!");
        } else {
            existingBooking.setBookingStatus(BookingStatus.CHECKED_IN);
        }

        // set check-in/check-out date
        existingBooking.setCheckInDate(UtilityMethods.setLocalDate(checkInRequest.actualCheckInDate()));
        existingBooking.setCheckOutDate(UtilityMethods.setLocalDate(checkInRequest.actualCheckOutDate()));

        // set RoomStatus
        existingBooking.getRoom().setRoomStatus(RoomStatus.OCCUPIED);
//        roomRepository.save(existingBooking.getRoom());

        // create BookingHistory
        BookingHistory bookingHistory = BookingHistory.builder()
                .actualCheckInDate(existingBooking.getCheckInDate())
                .actualCheckOutDate(existingBooking.getCheckOutDate())
                .historyType(HistoryType.CHECK_IN)
                .note((checkInRequest.note() == null || checkInRequest.note().isBlank()) ?
                        "Check-in" : checkInRequest.note())
                .booking(existingBooking)
                .room(existingBooking.getRoom())
                .build();

        // save Booking and BookingHistory
        Booking updatedBooking = bookingRepository.save(existingBooking);
        bookingHistoryRepository.save(bookingHistory);

        return bookingToDTO(updatedBooking);
    }

    @Override
    public BookingDTO checkOut(Integer bookingId, CheckOutOrExtendRequest checkOutRequest) {
        // check for an existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        // ALLOW actualCheckOutDate IS NULL
        String actualCheckOutDate = checkOutRequest.actualCheckOutDate();

        // check-in date before check-out date
        if (actualCheckOutDate != null && !actualCheckOutDate.isBlank()) {
            UtilityMethods.check_inIsBeforeCheck_out(existingBooking.getCheckInDate().toString(), checkOutRequest.actualCheckOutDate());
        }

        // change BookingStatus to CHECKED_OUT
        if (existingBooking.getBookingStatus() != BookingStatus.CHECKED_IN) {
            throw new BookingStatusException("This booking has not been checked out!");
        } else {
            existingBooking.setBookingStatus(BookingStatus.CHECKED_OUT);
        }

        // set check-out date
        if (actualCheckOutDate != null && !actualCheckOutDate.isBlank()) {
            existingBooking.setCheckOutDate(UtilityMethods.setLocalDate(actualCheckOutDate));
        }

        // set RoomStatus
        existingBooking.getRoom().setRoomStatus(RoomStatus.DIRTY);

        // create BookingHistory
        BookingHistory bookingHistory = BookingHistory.builder()
                .actualCheckInDate(existingBooking.getCheckInDate())
                .actualCheckOutDate(existingBooking.getCheckOutDate())
                .historyType(HistoryType.CHECK_OUT)
                .note((checkOutRequest.note() == null || checkOutRequest.note().isBlank()) ?
                        "Check-out" : checkOutRequest.note())
                .finalTotalPrice(finalTotalPrice(bookingId))
                .booking(existingBooking)
                .room(existingBooking.getRoom())
                .build();
        
        // save updatedBooking and BookingHistory
        Booking updatedBooking = bookingRepository.save(existingBooking);
        bookingHistoryRepository.save(bookingHistory);

        BookingDTO bookingDTO = bookingToDTO(updatedBooking);
        bookingDTO.setFinalTotalPrice(bookingHistory.getFinalTotalPrice().toString());

        return bookingDTO;
    }

    @Override
    public BookingDTO extendStay(Integer bookingId, CheckOutOrExtendRequest extendStayRequest) {
        // check for an existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        // check bookingStatus equal CHECKED_IN
        if (existingBooking.getBookingStatus() != BookingStatus.CHECKED_IN) {
            throw new BookingStatusException("This booking has not been checked in!");
        }

        // extend date
        LocalDate newCheckOutDate = UtilityMethods.setLocalDate(extendStayRequest.actualCheckOutDate());
        
        // new check out must after old check out
        if (!newCheckOutDate.isAfter(existingBooking.getCheckOutDate())) {
            throw new CustomLocalDateException("The new check-out date must after the old check-out date!");
        }

        //kiem tra chong cheo
        List<Booking> confirmedBookings = bookingRepository.findByRoomAndBookingStatus(
                existingBooking.getRoom(), BookingStatus.CONFIRMED);
        for ( Booking booking : confirmedBookings ) {
            if (newCheckOutDate.isAfter(booking.getCheckInDate())) {
                throw new CustomLocalDateException("Cannot extend stay. The room is not available for the requested dates.");
            }
        }
        
        // set new check-out date
        existingBooking.setCheckOutDate(newCheckOutDate);

        // create BookingHistory
        BookingHistory bookingHistory = BookingHistory.builder()
                .actualCheckInDate(existingBooking.getCheckInDate())
                .actualCheckOutDate(existingBooking.getCheckOutDate())
                .historyType(HistoryType.EXTEND_STAY)
                .note((extendStayRequest.note() == null || extendStayRequest.note().isBlank()) ?
                        "Extended stay" : extendStayRequest.note())
                .booking(existingBooking)
                .room(existingBooking.getRoom())
                .build();

        // save updatedBooking and BookingHistory
        Booking updatedBooking = bookingRepository.save(existingBooking);
        bookingHistoryRepository.save(bookingHistory);

        return bookingToDTO(updatedBooking);
    }

    @Override
    public BookingDTO changeRoom(Integer bookingId, Integer newRoomId, ChangeRoomRequest changeRoomRequest) {
        // check for an existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        // check for an existing room
        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(newRoomId)));

        // Check for duplicate rooms
        if (newRoom.equals(existingBooking.getRoom())) {
            throw new RoomNotAvailableException("The new room must be different from the current room!");
        }

        // check for THE NEW ROOM is available OR reserved, Rooms that have already been checked in will not be considered
        if (newRoom.getRoomStatus() != RoomStatus.AVAILABLE && newRoom.getRoomStatus() != RoomStatus.RESERVED) {
            throw new RoomNotAvailableException("Room " + newRoom.getRoomNumber() + " is not ready for guests to change to!");
        }

        // check for booking status
        BookingStatus bookingStatus = existingBooking.getBookingStatus();
        if (bookingStatus != BookingStatus.CONFIRMED && bookingStatus != BookingStatus.CHECKED_IN) {
            throw new BookingStatusException("This booking has not been confirmed or checked-in!");
        }

        // check for the new change date must NOT BE BEFORE the old change date
        // gợi ý: get ra bookingHistory mới nhất có changeDate != null
        LocalDate changeDate = UtilityMethods.setLocalDate(changeRoomRequest.changeDate());
        BookingHistory lastChangeRoom = bookingHistoryRepository.findLastChangeRoom(existingBooking);

        // changeDate cannot be before check-in date and must be before check-out date
        if (changeDate.isBefore(existingBooking.getCheckInDate())
                || !changeDate.isBefore(existingBooking.getCheckOutDate())) {
            throw new CustomLocalDateException("Room change date cannot be before check-in date AND must be before check-out date!");
        }

        // there are room changes
        if (lastChangeRoom != null) {
            if (changeDate.isBefore(lastChangeRoom.getChangeDate())) {
                throw new CustomLocalDateException("New room change date cannot be before old room change date!");
            }
        }

        // kiem tra chong cheo
        if (bookingStatus == BookingStatus.CONFIRMED) {
            if (!isRoomAvailable(newRoom, existingBooking.getCheckInDate(), existingBooking.getCheckOutDate())) {
                throw new RoomNotAvailableException("Room " + newRoom.getRoomNumber() + " is not available for the specified dates.");
            }
        } else if (bookingStatus == BookingStatus.CHECKED_IN) {
            if (!isRoomAvailable(newRoom, changeDate, existingBooking.getCheckOutDate())) {
                throw new RoomNotAvailableException("Room " + newRoom.getRoomNumber() + " is not available for the specified dates.");
            }
        }

        // update status and save the old room
        Room oldRoom = existingBooking.getRoom();
        oldRoom.setRoomStatus(RoomStatus.MAINTENANCE);
        roomRepository.save(oldRoom);

        // set status for THE NEW ROOM: CONFIRMED<->RESERVED or CHECKED_IN<->OCCUPIED
        newRoom.setRoomStatus(bookingStatus == BookingStatus.CONFIRMED ? RoomStatus.RESERVED : RoomStatus.OCCUPIED);

        // update booking
        existingBooking.setRoom(newRoom);

        // create bookingHistory
        BookingHistory bookingHistory = BookingHistory.builder()
                .actualCheckInDate(existingBooking.getCheckInDate())
                .actualCheckOutDate(existingBooking.getCheckOutDate())
                .historyType(HistoryType.CHANGE_ROOM)
                .note(changeRoomRequest.note() == null || changeRoomRequest.note().isBlank() ?
                        "Changed room" : changeRoomRequest.note())
                .changeDate(bookingStatus == BookingStatus.CONFIRMED ?
                        existingBooking.getCheckInDate() : changeDate)
                // nếu Booking status == CONFIRMED chứ ko phải CHECKED-IN thì khi changeRoom BẮT BUỘC changeDate phải = checkInDate
                .booking(existingBooking)
                .room(newRoom)
                .build();

        // save
        Booking updatedBooking = bookingRepository.save(existingBooking);
        bookingHistoryRepository.save(bookingHistory);

        return bookingToDTO(updatedBooking);
    }

    @Override
    public BookingDTO cancelBooking(Integer bookingId, String note) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        // check booking status equal CONFIRMED
        if (existingBooking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BookingStatusException("This booking cannot be canceled as it has not been confirmed.");
        }

        // set booking status
        existingBooking.setBookingStatus(BookingStatus.CANCELLED);

        // set room status
        List<Booking> hasOtherBooking = bookingRepository.findByRoomAndBookingStatus(
                existingBooking.getRoom(), BookingStatus.CONFIRMED);

        if (hasOtherBooking.size() == 1) {
            existingBooking.getRoom().setRoomStatus(RoomStatus.AVAILABLE);
        } else {
            existingBooking.getRoom().setRoomStatus(RoomStatus.RESERVED);
        }

        // create bookingHistory
        BookingHistory bookingHistory = BookingHistory.builder()
                .actualCheckInDate(existingBooking.getCheckInDate())
                .actualCheckOutDate(existingBooking.getCheckOutDate())
                .historyType(HistoryType.CANCEL)
                .note((note == null || note.isBlank()) ? "Cancel" : note)
                .booking(existingBooking)
                .room(existingBooking.getRoom())
                .build();

        // save booking and bookingHistory
        Booking updatedBooking = bookingRepository.save(existingBooking);
        bookingHistoryRepository.save(bookingHistory);

        return bookingToDTO(updatedBooking);
    }

    // Delete
    @Override
    public Map<String, String> deleteBooking(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        bookingRepository.delete(existingBooking);

        return Map.of("Message", "Booking with Id " + bookingId + " has been deleted successfully!");
    }

    // utils
    private Boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        // kiem tra chong cheo
        List<Booking> cfBookings = bookingRepository.findByRoomAndBookingStatus(room, BookingStatus.CONFIRMED);
        List<Booking> ciBookings = bookingRepository.findByRoomAndBookingStatus(room, BookingStatus.CHECKED_IN);

        List<Booking> mergedList = Stream.concat(cfBookings.stream(), ciBookings.stream()).toList();

        for (Booking booking : mergedList) {
            if (checkInDate.isBefore(booking.getCheckOutDate()) && checkOutDate.isAfter(booking.getCheckInDate())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public BookingDTO bookingToDTO(Booking booking) {
        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .checkInDate(String.valueOf(booking.getCheckInDate()))
                .checkOutDate(String.valueOf(booking.getCheckOutDate()))
                .bookingStatus(String.valueOf(booking.getBookingStatus()))
                .bookingVoucher(String.valueOf(booking.getBookingVoucher()))
                .customerName(booking.getCustomer().getLastName() + " " + booking.getCustomer().getFirstName())
                .roomNumber(booking.getRoom().getRoomNumber())
                .serviceUsageDTOs(booking.getServiceUsages().stream().map(
                        sus::serviceUsageToDTO).collect(Collectors.toList()))
                .build();
    }
}
