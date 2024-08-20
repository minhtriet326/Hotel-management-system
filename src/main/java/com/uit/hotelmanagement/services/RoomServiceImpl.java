package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.RoomDTO;
import com.uit.hotelmanagement.entities.Photo;
import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.exceptions.FileServiceException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.RoomRepository;
import com.uit.hotelmanagement.utils.RoomStatus;
import com.uit.hotelmanagement.utils.RoomType;
import com.uit.hotelmanagement.utils.records.RoomPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final PhotoService photoService;
    private RoomType setRoomType(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 6) {
            throw new ResourceNotFoundException("Room type", "roomType", index);
        }

        RoomType roomType;

        switch (Integer.parseInt(index)) {
            case 1:
                roomType = RoomType.SINGLE;
                break;
            case 2:
                roomType = RoomType.DOUBLE;
                break;
            case 3:
                roomType = RoomType.TWIN;
                break;
            case 4:
                roomType = RoomType.TRIPLE;
                break;
            case 5:
                roomType = RoomType.QUAD;
                break;
            default:
                roomType = RoomType.FAMILY;
                break;
        }
        return roomType;
    }
    private RoomStatus setRoomStatus(String index) {
        if (Integer.parseInt(index) < 1 || Integer.parseInt(index) > 5) {
            throw new ResourceNotFoundException("Room status", "roomStatus", index);
        }

        RoomStatus status;
        
        switch (Integer.parseInt(index)) {
            case 1:
                status = RoomStatus.AVAILABLE;
                break;
            case 2:
                status = RoomStatus.RESERVED;
                break;
            case 3:
                status = RoomStatus.OCCUPIED;
                break;
            case 4:
                status = RoomStatus.DIRTY;
                break;
            default:
                status = RoomStatus.MAINTENANCE;
                break;
        }
        return status;
    }
    @Override
    public RoomDTO createRoom(RoomDTO roomDTO, MultipartFile[] files) {

        if (files != null && files.length > 5) {
            throw new FileServiceException("Can't upload more than 5 photos!");
        }

        Room newRoom = Room.builder()
                .roomNumber(roomDTO.getRoomNumber())
                .roomType(setRoomType(roomDTO.getRoomType()))
                .price(new BigDecimal(String.valueOf(roomDTO.getPrice())))
                .roomStatus(setRoomStatus(roomDTO.getRoomStatus()))
                .photos(new ArrayList<>())
                .build();

        Room firstSavedRoom = roomRepository.save(newRoom);

        if (files != null) {
            List<Photo> photos;

            photos = photoService.saveAllPhotos(firstSavedRoom, files);

            firstSavedRoom.setPhotos(photos);
        }

        Room lastSavedRoom = roomRepository.save(firstSavedRoom);

        return roomToDTO(lastSavedRoom);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(this::roomToDTO).collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Integer roomId) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));
        return roomToDTO(existingRoom);
    }

    @Override
    public RoomDTO getRoomByRoomNumber(String roomNumber) {
        Room existingRoom = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "room number", roomNumber));

        return roomToDTO(existingRoom);
    }

    @Override
    public List<RoomDTO> getAllRoomsByRoomType(String roomTypeIndex) {
        RoomType roomType = setRoomType(roomTypeIndex);

        List<Room> existingRooms = roomRepository.findByRoomType(roomType)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "room type", String.valueOf(roomType)));

        return existingRooms.stream().map(this::roomToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAllRoomsByPriceBetween(String minPrice, String maxPrice) {
        List<Room> existingRooms = roomRepository.findByPriceBetween(new BigDecimal(minPrice), new BigDecimal(maxPrice));

        return existingRooms.stream().map(this::roomToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAllRoomsByRoomStatus(String roomStatusIndex) {
        RoomStatus roomStatus = setRoomStatus(roomStatusIndex);

        List<Room> existingRooms = roomRepository.findByRoomStatus(roomStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "room status", String.valueOf(roomStatus)));

        return existingRooms.stream().map(this::roomToDTO).collect(Collectors.toList());
    }

    @Override
    public RoomPageResponse getAllRoomsWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Room> roomPage = roomRepository.findAll(pageable);

        List<Room> rooms = roomPage.getContent();

        return RoomPageResponse.builder()
                .roomDTOs(rooms.stream().map(this::roomToDTO).collect(Collectors.toList()))
                .pageNumber(roomPage.getNumber())
                .pageSize(roomPage.getSize())
                .totalElements(roomPage.getTotalElements())
                .totalPages(roomPage.getTotalPages())
                .isLast(roomPage.isLast())
                .build();
    }

    @Override
    public RoomPageResponse getAllRoomsWithPaginationAndSorting(Integer pageNumber, Integer pageSize,
                                                                String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Room> roomPage = roomRepository.findAll(pageable);

        List<Room> rooms = roomPage.getContent();

        return RoomPageResponse.builder()
                .roomDTOs(rooms.stream().map(this::roomToDTO).collect(Collectors.toList()))
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(roomPage.getTotalElements())
                .totalPages(roomPage.getTotalPages())
                .isLast(roomPage.isLast())
                .build();
    }

    @Override
    public RoomDTO updateRoom(Integer roomId, RoomDTO roomDTO, MultipartFile[] files) {
        if (files != null && files.length > 5) {
            throw new FileServiceException("Can't upload more than 5 photos!");
        }

        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        if (files != null) {
            photoService.deleteAllPhotos(existingRoom);
            photoService.saveAllPhotos(existingRoom, files);
        }

        existingRoom.setRoomNumber(roomDTO.getRoomNumber());
        existingRoom.setRoomType(setRoomType(roomDTO.getRoomType()));
        existingRoom.setPrice(new BigDecimal(String.valueOf(roomDTO.getPrice())));
        existingRoom.setRoomStatus(setRoomStatus(roomDTO.getRoomStatus()));

        Room updatedRoom = roomRepository.save(existingRoom);

        return roomToDTO(updatedRoom);
    }

    @Override
    public Map<String, String> deleteRoom(Integer roomId) {
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomId", Integer.toString(roomId)));

        roomRepository.delete(existingRoom);

        return Map.of("Message", "Room with Id " + roomId + " has been deleted successfully!");
    }

    private RoomDTO roomToDTO(Room room) {
        return RoomDTO.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomType(String.valueOf(room.getRoomType()))
                .price(Double.parseDouble(room.getPrice().toString()))
                .roomStatus(String.valueOf(room.getRoomStatus()))
                .photoDTOs(room.getPhotos().stream().map(
                        photoService::photoToDTO).collect(Collectors.toList()))
                .build();
    }
}
