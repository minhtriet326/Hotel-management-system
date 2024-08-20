package com.uit.hotelmanagement.utils.records;

import jakarta.validation.constraints.NotNull;

public record CheckOutOrExtendRequest(
        @NotNull(message = "Actual check out date can't be null")
        String actualCheckOutDate,
        String note
) {
}
