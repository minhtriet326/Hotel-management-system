package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Payment;
import com.uit.hotelmanagement.utils.PaymentMethod;
import com.uit.hotelmanagement.utils.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByPaymentDate(LocalDate paymentDate);
    List<Payment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    Payment findByBooking(Booking booking);
}
