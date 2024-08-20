package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.PaymentDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Payment;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.BookingRepository;
import com.uit.hotelmanagement.repositories.PaymentRepository;
import com.uit.hotelmanagement.utils.PaymentMethod;
import com.uit.hotelmanagement.utils.PaymentStatus;
import com.uit.hotelmanagement.utils.UtilityMethods;
import com.uit.hotelmanagement.utils.records.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final BookingService bookingService;
    private final ServiceUsageService sus;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private PaymentMethod setPaymentMethod(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 2) {
            throw new ResourceNotFoundException("Payment method", "Payment method index", index);
        }

        if (Integer.parseInt(index) == 1) {
            return PaymentMethod.CREDIT_CARD;
        } else {
            return PaymentMethod.CASH;
        }
    }
    private PaymentStatus setPaymentStatus(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 3) {
            throw new ResourceNotFoundException("Payment status", "Payment status index", index);
        }

        return switch (Integer.parseInt(index)) {
            case 1 -> PaymentStatus.COMPLETED;
            case 2 -> PaymentStatus.PENDING;
            default -> PaymentStatus.FAILED;
        };
    }

    @Override
    public PaymentDTO createPayment(Integer bookingId, PaymentRequest paymentRequest) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        Payment newPayment = Payment.builder()
                .paymentDate(UtilityMethods.setLocalDate(paymentRequest.paymentDate()))
                .paymentMethod(setPaymentMethod(paymentRequest.paymentMethod()))
                .paymentStatus(setPaymentStatus(paymentRequest.paymentStatus()))
                .booking(existingBooking)
                .build();

        BigDecimal bookingPrice = bookingService.finalTotalPrice(bookingId);
        BigDecimal totalServicePrice = sus.totalServicePriceOfBooking(bookingId);

        newPayment.setAmount(bookingPrice.add(totalServicePrice));

        Payment savedPayment = paymentRepository.save(newPayment);
        return paymentToDTO(savedPayment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::paymentToDTO).collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPaymentById(Integer paymentId) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", Integer.toString(paymentId)));
        return paymentToDTO(existingPayment);
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByPaymentDate(String paymentDate) {
        List<Payment> payments = paymentRepository.findByPaymentDate(UtilityMethods.setLocalDate(paymentDate));
        return payments.stream().map(this::paymentToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByAmountBetween(String minAmount, String maxAmount) {
        List<Payment> payments = paymentRepository.findByAmountBetween(new BigDecimal(minAmount), new BigDecimal(maxAmount));
        return payments.stream().map(this::paymentToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByPaymentMethod(String paymentMethodIndex) {
        List<Payment> payments = paymentRepository.findByPaymentMethod(setPaymentMethod(paymentMethodIndex));
        return payments.stream().map(this::paymentToDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByPaymentStatus(String paymentStatusIndex) {
        List<Payment> payments = paymentRepository.findByPaymentStatus(setPaymentStatus(paymentStatusIndex));
        return payments.stream().map(this::paymentToDTO).collect(Collectors.toList());
    }

    @Override
    public PaymentDTO getPaymentByBooking(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        Payment existingPayment = paymentRepository.findByBooking(existingBooking);
        return paymentToDTO(existingPayment);
    }

    @Override
    public PaymentDTO updatePayment(Integer paymentId, PaymentRequest paymentRequest) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", Integer.toString(paymentId)));

        existingPayment.setPaymentDate(UtilityMethods.setLocalDate(paymentRequest.paymentDate()));
        existingPayment.setPaymentStatus(setPaymentStatus(paymentRequest.paymentStatus()));
        existingPayment.setPaymentMethod(setPaymentMethod(paymentRequest.paymentMethod()));

        BigDecimal bookingPrice = bookingService.finalTotalPrice(existingPayment.getBooking().getBookingId());
        BigDecimal totalServicePrice = sus.totalServicePriceOfBooking(existingPayment.getBooking().getBookingId());

        existingPayment.setAmount(bookingPrice.add(totalServicePrice));

        Payment updatedPayment = paymentRepository.save(existingPayment);

        return paymentToDTO(updatedPayment);
    }

    @Override
    public Map<String, String> deletePayment(Integer paymentId) {
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentId", Integer.toString(paymentId)));

        paymentRepository.delete(existingPayment);

        return Map.of("Message", "Payment with Id " + paymentId + " has been deleted successfully!");
    }

    private PaymentDTO paymentToDTO(Payment payment) {
        PaymentDTO paymentDTO =  PaymentDTO.builder()
                .paymentId(payment.getPaymentId())
                .paymentDate(payment.getPaymentDate().toString())
                .amount(payment.getAmount().toString())
                .paymentMethod(payment.getPaymentMethod().toString())
                .paymentStatus(payment.getPaymentStatus().toString())
                .bookingDTO(bookingService.bookingToDTO(payment.getBooking()))
                .build();

        BigDecimal bookingPrice = bookingService.finalTotalPrice(payment.getBooking().getBookingId());
        paymentDTO.getBookingDTO().setFinalTotalPrice(bookingPrice.toString());

        return paymentDTO;
    }
}
