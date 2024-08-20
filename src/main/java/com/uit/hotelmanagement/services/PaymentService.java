package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.PaymentDTO;
import com.uit.hotelmanagement.utils.records.PaymentRequest;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    // Post
    PaymentDTO createPayment(Integer bookingId, PaymentRequest paymentRequest);
    // Get
    List<PaymentDTO> getAllPayments();
    PaymentDTO getPaymentById(Integer paymentId);
    List<PaymentDTO> getAllPaymentsByPaymentDate(String paymentDate);
    List<PaymentDTO> getAllPaymentsByAmountBetween(String minAmount, String maxAmount);
    List<PaymentDTO> getAllPaymentsByPaymentMethod(String paymentMethod);
    List<PaymentDTO> getAllPaymentsByPaymentStatus(String paymentStatus);
    PaymentDTO getPaymentByBooking(Integer bookingId);
    // Put
    PaymentDTO updatePayment(Integer paymentId, PaymentRequest paymentRequest);
    // Delete
    Map<String, String> deletePayment(Integer paymentId);
}
