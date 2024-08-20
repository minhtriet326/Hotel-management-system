package com.uit.hotelmanagement.utils.records;

import jakarta.validation.constraints.NotBlank;

public record ChangeRoomRequest(
        @NotBlank(message = "Room change date can't be blank")
        String changeDate,
        String note
) {
}
