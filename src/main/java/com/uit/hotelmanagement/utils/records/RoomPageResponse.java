package com.uit.hotelmanagement.utils.records;

import com.uit.hotelmanagement.dtos.RoomDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record RoomPageResponse(
        List<RoomDTO> roomDTOs,
        Integer pageNumber,
        Integer pageSize,
        long totalElements,
        Integer totalPages,
        Boolean isLast
) {
}
