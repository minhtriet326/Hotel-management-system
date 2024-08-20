package com.uit.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "service_usage")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServiceUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceUsageId;

    @Positive(message = "The number of service users must be a positive number")
    @Min(value = 1, message = "The number of service users must be at least 1 person")
    private Integer numOfUsers;

    @NotNull(message = "Start date can't be null")
    private LocalDate startDate;

    @NotNull(message = "End date can't be null")
    private LocalDate endDate;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    @Digits(integer = 1, fraction = 2)
    private BigDecimal serviceVoucher;

    @PositiveOrZero(message = "Total service price cannot be negative")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

//    Methods
    public BigDecimal calculateTotalPrice() {
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);

        Integer numOfUsers = this.numOfUsers;

        BigDecimal voucher = BigDecimal.ONE.subtract(serviceVoucher);

        BigDecimal servicePrice = service.getPrice();

        return servicePrice
                .multiply(voucher)
                .multiply(BigDecimal.valueOf(numOfUsers))
                .multiply(BigDecimal.valueOf(numOfDays).add(BigDecimal.ONE))
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
