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
public class PaymentDTO {
    private Integer paymentId;

    @NotBlank(message = "Payment date can't be blank")
    private String paymentDate;

    private String amount;

    @NotBlank(message = "Payment method can't be blank")
    @Min(value = 1, message = "Payment method must be an integer between 1 and 2")
    @Max(value = 2, message = "Payment method must be an integer between 1 and 2")
    private String paymentMethod;

    @NotBlank(message = "Payment status cannot be blank")
    @Min(value = 1, message = "Payment status must be an integer between 1 and 3")
    @Max(value = 3, message = "Payment status must be an integer between 1 and 3")
    private String paymentStatus;

    private BookingDTO bookingDTO;
}
