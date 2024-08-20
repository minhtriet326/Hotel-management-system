package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.BookingDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.BookingHistory;
import com.uit.hotelmanagement.utils.records.ChangeRoomRequest;
import com.uit.hotelmanagement.utils.records.CheckInRequest;
import com.uit.hotelmanagement.utils.records.CheckOutOrExtendRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BookingService {
    // Post
    BookingDTO reserveRoom(Integer customerId, Integer roomId, BookingDTO bookingDTO);
    // Get
    List<BookingDTO> getAllBookings();
    BookingDTO getBookingById(Integer bookingId);
    List<BookingDTO> getAllBookingsByCheckInDate(String checkInDate);
    List<BookingDTO> getAllBookingsByCheckOutDate(String checkOutDate);
    List<BookingDTO> getAllBookingsByBookingStatus(String bookingStatusIndex);
    List<BookingDTO> getAllBookingsByCustomer(Integer customerId);
    List<BookingDTO> getAllBookingsByRoom(Integer roomId);
    BigDecimal finalTotalPrice(Integer bookingId);
    // Put
    BookingDTO updateBooking(Integer bookingId, Integer roomId, BookingDTO bookingDTO);// change room NO change customer
    BookingDTO checkIn(Integer bookingId, CheckInRequest checkInRequest);
    BookingDTO checkOut(Integer bookingId, CheckOutOrExtendRequest checkOutRequest);
    BookingDTO extendStay(Integer bookingId, CheckOutOrExtendRequest extendStayRequest);
    BookingDTO changeRoom(Integer bookingId, Integer newRoomId, ChangeRoomRequest changeRoomRequest);
    BookingDTO cancelBooking(Integer bookingId, String note);
    // Delete
    Map<String, String> deleteBooking(Integer bookingId);
    // Features
    BookingDTO bookingToDTO(Booking booking);
}
