package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.BookingHistoryDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.BookingHistory;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.exceptions.BookingStatusException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.BookingHistoryRepository;
import com.uit.hotelmanagement.repositories.BookingRepository;
import com.uit.hotelmanagement.repositories.RoomRepository;
import com.uit.hotelmanagement.utils.HistoryType;
import com.uit.hotelmanagement.utils.UtilityMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingHistoryServiceImpl implements BookingHistoryService{
    private final BookingHistoryRepository bookingHistoryRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private HistoryType setHistoryType(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 5) {
            throw new ResourceNotFoundException("History type", "index", index);
        }

        return switch (Integer.parseInt(index)) {
            case 1 -> HistoryType.CHECK_IN;
            case 2 -> HistoryType.CHECK_OUT;
            case 3 -> HistoryType.EXTEND_STAY;
            case 4 -> HistoryType.CHANGE_ROOM;
            default -> HistoryType.CANCEL;
        };
    }
    @Override
    public List<BookingHistoryDTO> getAllBookingHistories() {
        return bookingHistoryRepository.findAll().stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public BookingHistoryDTO getBookingHistoryById(Integer bookHistoryId) {
        BookingHistory existingBookingHistory = bookingHistoryRepository.findById(bookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking history", "bookHistoryId",
                        Integer.toString(bookHistoryId)));

        return bookingHistoryToDTO(existingBookingHistory);
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByActualCheckInDate(String checkInDate) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByActualCheckInDate(UtilityMethods.setLocalDate(checkInDate));
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByActualCheckOutDate(String checkOutDate) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByActualCheckOutDate(UtilityMethods.setLocalDate(checkOutDate));
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByHistoryType(String historyTypeIndex) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByHistoryType(setHistoryType(historyTypeIndex));
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByNote(String keyword) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByNoteContaining(keyword);
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByChangeDate(String changeDate) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByChangeDate(UtilityMethods.setLocalDate(changeDate));
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByPriceBetween(String minPrice, String maxPrice) {
        List<BookingHistory> bookingHistories = bookingHistoryRepository.findByFinalTotalPriceBetween(
                new BigDecimal(minPrice), new BigDecimal(maxPrice));
        return bookingHistories.stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByBooking(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        return bookingHistoryRepository.findByBooking(existingBooking).stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingHistoryDTO> getAllBookingHistoriesByRoom(Integer roomId) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        return bookingHistoryRepository.findByRoom(existingRoom).stream().map(this::bookingHistoryToDTO).collect(Collectors.toList());
    }

    @Override
    public BookingHistoryDTO updateBookingHistory(Integer bookHistoryId, BookingHistoryDTO bookingHistoryDTO) {
        BookingHistory existingBookingHistory = bookingHistoryRepository.findById(bookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking history", "bookHistoryId",
                        Integer.toString(bookHistoryId)));

        existingBookingHistory.setActualCheckInDate(UtilityMethods.setLocalDate(bookingHistoryDTO.getActualCheckInDate()));
        existingBookingHistory.setActualCheckOutDate(UtilityMethods.setLocalDate(bookingHistoryDTO.getActualCheckOutDate()));
        existingBookingHistory.setHistoryType(setHistoryType(bookingHistoryDTO.getHistoryType()));
        existingBookingHistory.setNote(bookingHistoryDTO.getNote());
        existingBookingHistory.setChangeDate(UtilityMethods.setLocalDate(bookingHistoryDTO.getChangeDate()));
        existingBookingHistory.setFinalTotalPrice(BigDecimal.valueOf(bookingHistoryDTO.getFinalTotalPrice()));

        BookingHistory updatedBookingHistory = bookingHistoryRepository.save(existingBookingHistory);

        return bookingHistoryToDTO(updatedBookingHistory);
    }

    @Override
    public Map<String, String> deleteBookingHistory(Integer bookHistoryId) {
        BookingHistory existingBookingHistory = bookingHistoryRepository.findById(bookHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking history", "bookHistoryId",
                        Integer.toString(bookHistoryId)));

        bookingHistoryRepository.delete(existingBookingHistory);

        return Map.of("Message", "Booking history with Id " + bookHistoryId + " has been deleted successfully!");
    }

    private BookingHistoryDTO bookingHistoryToDTO(BookingHistory bookingHistory) {
        return BookingHistoryDTO.builder()
                .bookHistoryId(bookingHistory.getBookHistoryId())
                .actualCheckInDate(bookingHistory.getActualCheckInDate().toString())
                .actualCheckOutDate(bookingHistory.getActualCheckOutDate().toString())
                .historyType(bookingHistory.getHistoryType().toString())
                .note(bookingHistory.getNote())
                .changeDate(bookingHistory.getChangeDate() == null ?
                        null : bookingHistory.getChangeDate().toString())
                .finalTotalPrice(bookingHistory.getFinalTotalPrice() == null ?
                        null : bookingHistory.getFinalTotalPrice().doubleValue())
                .bookingId(bookingHistory.getBooking().getBookingId())
                .roomNumber(bookingHistory.getRoom().getRoomNumber())
                .build();
    }
}
