package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServiceUsageDTO {
    private Integer serviceUsageId;

    @Positive(message = "The number of service users must be a positive number")
    @Min(value = 1, message = "The number of service users must be at least 1 person")
    private Integer numOfUsers;

    @NotNull(message = "Start date can't be null")
    private String startDate;

    @NotNull(message = "End date can't be null")
    private String endDate;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    @Digits(integer = 1, fraction = 2)
    private String serviceVoucher;

    private String totalPrice;

    private String serviceName;

    private Integer bookingId;
}
