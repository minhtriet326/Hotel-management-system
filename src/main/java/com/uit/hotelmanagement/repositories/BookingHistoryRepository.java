package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.BookingHistory;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.utils.HistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Integer> {
    @Query("SELECT bh FROM BookingHistory bh " +
            "WHERE bh.booking = ?1 " +
            "AND bh.historyType = ?2 " +
            "AND bh.changeDate >= ?3 " +
            "ORDER BY bh.changeDate, bh.bookHistoryId")
    List<BookingHistory> findByBookingWithHistoryTypeAndChangeDate(Booking booking, HistoryType historyType, LocalDate checkInDate);

    // get bookingHistory with historyType = CHECK_IN
    Optional<BookingHistory> findFirstByBookingAndHistoryType(Booking booking, HistoryType checkInType);

    BookingHistory findFirstByBookingOrderByBookHistoryIdDesc(Booking booking);

    @Query("SELECT bh FROM BookingHistory bh WHERE bh.booking = ?1 " +
            "AND bh.changeDate = (SELECT MAX(bh2.changeDate) FROM BookingHistory bh2 WHERE bh2.booking = ?1) " +
            "ORDER BY bh.bookHistoryId DESC")
    List<BookingHistory> findTopByBookingWithMaxChangeDateAndHighestBookHistoryId(Booking booking);

    default BookingHistory findLastChangeRoom(Booking booking) {
        List<BookingHistory> bookingHistories = findTopByBookingWithMaxChangeDateAndHighestBookHistoryId(booking);
        return bookingHistories.isEmpty() ? null : bookingHistories.getFirst();
    }

    // Get
    List<BookingHistory> findByActualCheckInDate(LocalDate actualCheckInDate);
    List<BookingHistory> findByActualCheckOutDate(LocalDate actualCheckOutDate);
    List<BookingHistory> findByHistoryType(HistoryType historyType);
    List<BookingHistory> findByChangeDate(LocalDate changeDate);
    List<BookingHistory> findByNoteContaining(String keyword);
    List<BookingHistory> findByFinalTotalPriceBetween(BigDecimal minTotalPrice, BigDecimal maxTotalPrice);
    List<BookingHistory> findByBooking(Booking booking);
    List<BookingHistory> findByRoom(Room room);
}
