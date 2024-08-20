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
public class RoomDTO {
    private Integer roomId;

    @NotBlank(message = "Room number can't be blank")
    private String roomNumber;

    @NotBlank(message = "Room type can't be blank")
    @Min(value = 1, message = "Room type must be an integer between 1 and 6")
    @Max(value = 6, message = "Room type must be an integer between 1 and 6")
    private String roomType;

    @PositiveOrZero(message = "Room price can't be negative")
    private Double price;

    @NotBlank(message = "Room's status can't be blank")
    @Min(value = 1, message = "Room status must be an integer between 1 and 5")
    @Max(value = 5, message = "Room status must be an integer between 1 and 5")
    private String roomStatus;

    private List<PhotoDTO> photoDTOs;
}
