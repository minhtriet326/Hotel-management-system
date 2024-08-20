package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.BookingHistoryDTO;
import com.uit.hotelmanagement.entities.BookingHistory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BookingHistoryService {
    // Get
    List<BookingHistoryDTO> getAllBookingHistories();
    BookingHistoryDTO getBookingHistoryById(Integer bookHistoryId);
    List<BookingHistoryDTO> getAllBookingHistoriesByActualCheckInDate(String checkInDate);
    List<BookingHistoryDTO> getAllBookingHistoriesByActualCheckOutDate(String checkOutDate);
    List<BookingHistoryDTO> getAllBookingHistoriesByHistoryType(String historyTypeIndex);
    List<BookingHistoryDTO> getAllBookingHistoriesByNote(String keyword);
    List<BookingHistoryDTO> getAllBookingHistoriesByChangeDate(String changeDate);
    List<BookingHistoryDTO> getAllBookingHistoriesByPriceBetween(String minPrice, String maxPrice);
    List<BookingHistoryDTO> getAllBookingHistoriesByBooking(Integer bookingId);
    List<BookingHistoryDTO> getAllBookingHistoriesByRoom(Integer roomId);

    // Put
    BookingHistoryDTO updateBookingHistory(Integer bookHistoryId, BookingHistoryDTO bookingHistoryDTO);
    // Delete
    Map<String, String> deleteBookingHistory(Integer bookHistoryId);
}
