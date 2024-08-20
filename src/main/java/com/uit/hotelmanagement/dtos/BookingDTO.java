package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingDTO {
    private Integer bookingId;

    @NotNull(message = "Check in date can't be null")
    private String checkInDate;

    @NotNull(message = "Check in date can't be null")
    private String checkOutDate;

    @Min(value = 1, message = "Status must be an integer between 1 and 4")
    @Max(value = 4, message = "Status must be an integer between 1 and 4")
    private String bookingStatus;

    @DecimalMin(value = "0.0")// khong dat message thu coi no bao loi gi
    @DecimalMax(value = "1.0")
    @Digits(integer = 1, fraction = 2)
    private String bookingVoucher;

    private String finalTotalPrice;

    private String customerName;

    private String roomNumber;

    private List<ServiceUsageDTO> serviceUsageDTOs;

}
