package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookingHistoryDTO {
    private Integer bookHistoryId;

    @NotNull(message = "Actual check in date can't be null")
    private String actualCheckInDate;

    @NotNull(message = "Actual check out date can't be null")
    private String actualCheckOutDate;

    @NotBlank(message = "Booking history's type can't be blank")
    @Min(value = 1, message = "History type must be an integer between 1 and 5")
    @Max(value = 5, message = "History type must be an integer between 1 and 5")
    private String historyType;

    private String note;

    private String changeDate; //chỗ này nên có giờ cụ thể

    @PositiveOrZero(message = "Final total price can't be negative")
    private Double finalTotalPrice;

    private Integer bookingId;

    private String roomNumber;
}
