package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Customer;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.utils.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCheckInDate(LocalDate checkInDate);
    List<Booking> findByCheckOutDate(LocalDate checkInDate);
    List<Booking> findByBookingStatus(BookingStatus bookingStatus);
    List<Booking> findByCustomer(Customer customer);
    List<Booking> findByRoom(Room room);
    List<Booking> findByRoomAndBookingStatus(Room room, BookingStatus bookingStatus);
}
