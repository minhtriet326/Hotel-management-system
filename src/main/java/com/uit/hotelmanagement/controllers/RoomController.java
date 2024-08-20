package com.uit.hotelmanagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.hotelmanagement.dtos.RoomDTO;
import com.uit.hotelmanagement.services.RoomService;
import com.uit.hotelmanagement.utils.AppConstants;
import com.uit.hotelmanagement.utils.records.RoomPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @Operation(
            summary = "Create a room",
            description = "We will create a room by providing the roomDTO and the files",
            tags = {"Room", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = RoomDTO.class),
                            mediaType = "application/json")
            )
    })
    @PostMapping(value = "/createRoom", consumes = "multipart/form-data")
    public ResponseEntity<RoomDTO> createRoomHandler(@RequestPart String roomDTOstr,
                                                     @RequestPart(required = false) MultipartFile[] files) throws JsonProcessingException {
        RoomDTO roomDTO = this.convertJSONToRoomDTO(roomDTOstr);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(roomDTO, files));
    }

    @Operation(
            summary = "Get all rooms",
            description = "We will get all rooms",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRooms")
    public ResponseEntity<List<RoomDTO>> getAllRoomsHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRooms());
    }

    @Operation(
            summary = "Get a specific room by Id",
            description = "We will get a specific room by providing Id",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = RoomDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getRoomById/{roomId}")
    public ResponseEntity<RoomDTO> getRoomByIdHandler(@PathVariable Integer roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomById(roomId));
    }

    @Operation(
            summary = "Get a specific room by room number",
            description = "We will get a specific room by providing room number",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = RoomDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getRoomByRoomNumber/{roomNumber}")
    public ResponseEntity<RoomDTO> getRoomByRoomNumberHandler(@PathVariable String roomNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getRoomByRoomNumber(roomNumber));
    }

    @Operation(
            summary = "Get all rooms by room type",
            description = "We will get all rooms by providing room type",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRoomsByRoomType/{roomType}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByRoomTypeHandler(@PathVariable String roomType) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRoomsByRoomType(roomType));
    }

    @Operation(
            summary = "Get all rooms within a price range",
            description = "We will get all rooms with prices between minPrice and maxPrice",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRoomsByPriceBetween")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByPriceBetweenHandler(@RequestParam String minPrice,
                                                                          @RequestParam String maxPrice) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRoomsByPriceBetween(minPrice, maxPrice));
    }

    @Operation(
            summary = "Get all rooms by room status",
            description = "We will get all rooms by providing room status",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRoomsByRoomStatus/{roomStatus}")
    public ResponseEntity<List<RoomDTO>> getAllRoomsByRoomStatusHandler(@PathVariable String roomStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRoomsByRoomStatus(roomStatus));
    }

    @Operation(
            summary = "Get all room on a page",
            description = "We will get all rooms on a page by providing page number and page size",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = RoomPageResponse.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRoomsWithPagination")
    public ResponseEntity<RoomPageResponse> getAllRoomsWithPaginationHandler(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRoomsWithPagination(pageNumber, pageSize));
    }

    @Operation(
            summary = "Get all rooms on a page and sort them",
            description = "We will get all rooms on a page and sort them by providing page number, page size, sort field and direction",
            tags = {"Room", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = RoomPageResponse.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllRoomsWithPaginationAndSorting")
    public ResponseEntity<RoomPageResponse> getAllRoomsWithPaginationHandler(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.DIR) String dir) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.getAllRoomsWithPaginationAndSorting(
                pageNumber,
                pageSize,
                sortBy,
                dir));
    }

    @Operation(
            summary = "Update a specific room",
            description = "We will update a specific room by providing the Id, the roomDTO and the files",
            tags = {"Room", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = RoomDTO.class),
                            mediaType = "application/json")
            )
    })
    @PutMapping(value = "/updateRoom/{roomId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomDTO> updateRoomHandler(@PathVariable Integer roomId,
                                                     @RequestPart String roomDTOstr,
                                                     @RequestPart(required = false) MultipartFile[] files) throws JsonProcessingException {
        RoomDTO roomDTO = this.convertJSONToRoomDTO(roomDTOstr);
        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoom(roomId, roomDTO, files));
    }

    @Operation(
            summary = "Delete a specific room",
            description = "We will delete a specific room by providing Id",
            tags = {"Room", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                                        mediaType = "application/json")
            )
    })
    @DeleteMapping("/deleteRoom/{roomId}")
    public ResponseEntity<Map<String, String>> deleteRoomHandler(@PathVariable Integer roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(roomService.deleteRoom(roomId));
    }

    private RoomDTO convertJSONToRoomDTO(String roomDTOstr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(roomDTOstr, RoomDTO.class);
    }
}
