package com.uit.hotelmanagement.utils.records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PaymentRequest(
        @NotBlank(message = "Payment date can't be blank")
        String paymentDate,
        @NotBlank(message = "Payment method can't be blank")
        @Min(value = 1, message = "Payment method must be an integer between 1 and 2")
        @Max(value = 2, message = "Payment method must be an integer between 1 and 2")
        String paymentMethod,
        @NotBlank(message = "Payment status cannot be blank")
        @Min(value = 1, message = "Payment status must be an integer between 1 and 3")
        @Max(value = 3, message = "Payment status must be an integer between 1 and 3")
        String paymentStatus
) {
}
