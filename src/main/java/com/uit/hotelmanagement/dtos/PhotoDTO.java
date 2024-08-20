package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PhotoDTO {
    private Integer photoId;

    private String name;

    private String roomNumber;

    private String photoUrl;
}
