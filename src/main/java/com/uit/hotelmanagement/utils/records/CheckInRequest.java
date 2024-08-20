package com.uit.hotelmanagement.utils.records;

import jakarta.validation.constraints.NotNull;

public record CheckInRequest(
        @NotNull(message = "Actual check in date can't be null")
        String actualCheckInDate,
        @NotNull(message = "Actual check out date can't be null")
        String actualCheckOutDate,
        String note
) {}
