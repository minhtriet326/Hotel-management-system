package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.RoomDTO;
import com.uit.hotelmanagement.utils.records.RoomPageResponse;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RoomService {
    // Post
    RoomDTO createRoom(RoomDTO roomDTO, MultipartFile[] files);
    // Get
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Integer roomId);
    RoomDTO getRoomByRoomNumber(String roomNumber);
    List<RoomDTO> getAllRoomsByRoomType(String roomType);
    List<RoomDTO> getAllRoomsByPriceBetween(String minPrice, String maxPrice);
    List<RoomDTO> getAllRoomsByRoomStatus(String roomStatus);
    RoomPageResponse getAllRoomsWithPagination(Integer pageNumber, Integer pageSize);
    RoomPageResponse getAllRoomsWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);
    // Put
    RoomDTO updateRoom(Integer roomId, RoomDTO roomDTO, MultipartFile[] files);
    // Delete
    Map<String, String> deleteRoom(Integer roomId);
}
