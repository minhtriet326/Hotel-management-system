package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.BookingHistoryDTO;
import com.uit.hotelmanagement.services.BookingHistoryService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookingHistory")
public class BookingHistoryController {
    private final BookingHistoryService bookingHistoryService;
//  Get
    @Operation(
            summary = "Get all booking histories",
            description = "We will get all booking histories",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistories")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistories()
        );
    }

    @Operation(
            summary = "Get a specific booking history by Id",
            description = "We will get a specific booking history by providing Id",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingHistoryDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getBookingHistoryById/{bookHistoryId}")
    public ResponseEntity<BookingHistoryDTO> getBookingHistoryByIdHandler(@PathVariable Integer bookHistoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getBookingHistoryById(bookHistoryId));
    }

    @Operation(
            summary = "Get all booking histories by actual check-in date",
            description = "We will get all booking histories by providing actual check-in date",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByActualCheckInDate")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByActualCheckInDateHandler(@RequestParam String checkInDate) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByActualCheckInDate(checkInDate));
    }

    @Operation(
            summary = "Get all booking histories by actual check-out date",
            description = "We will get all booking histories by providing actual check-out date",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByActualCheckOutDate")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByActualCheckOutDateHandler(@RequestParam String checkOutDate) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByActualCheckOutDate(checkOutDate));
    }

    @Operation(
            summary = "Get all booking histories by history type",
            description = "We will get all booking histories by providing history type",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByHistoryType/{historyTypeIndex}")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByHistoryTypeHandler(String historyTypeIndex) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByHistoryType(historyTypeIndex)
        );
    }

    @Operation(
            summary = "Get all booking histories by note",
            description = "We will get all booking history whose notes contain a given keyword",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByNote")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByNoteHandler(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByNote(keyword)
        );
    }

    @Operation(
            summary = "Get all booking histories by changeDate",
            description = "We will get all booking histories by providing changeDate",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByChangeDate")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByChangeDateHandler(@RequestParam String changeDate) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByChangeDate(changeDate)
        );
    }

    @Operation(
            summary = "Get all booking histories by final total price",
            description = "We will get all booking histories whose final total price falls within a range",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByPriceBetween")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByPriceBetweenHandler(@RequestParam String minPrice,
                                                                                               @RequestParam String maxPrice) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.getAllBookingHistoriesByPriceBetween(minPrice, maxPrice)
        );
    }

    @Operation(
            summary = "Get all booking histories by booking",
            description = "We will get all booking histories of a specific booking",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByBooking/{bookingId}")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByBookingHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingHistoryService.getAllBookingHistoriesByBooking(bookingId));
    }

    @Operation(
            summary = "Get all booking histories by room",
            description = "We will get all booking histories of a specific room",
            tags = {"Booking history", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingHistoriesByRoom/{roomId}")
    public ResponseEntity<List<BookingHistoryDTO>> getAllBookingHistoriesByRoomHandler(@PathVariable Integer roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingHistoryService.getAllBookingHistoriesByRoom(roomId));
    }

//  Put
    @Operation(
            summary = "Update a specific booking history",
            description = "We will update a specific booking history by providing Id and bookingHistoryDTO",
            tags = {"Booking history", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingHistoryDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/updateBookingHistory/{bookHistoryId}")
    public ResponseEntity<BookingHistoryDTO> updateBookingHistoryHandler(@PathVariable Integer bookHistoryId,
                                                                         @Valid @RequestBody BookingHistoryDTO bookingHistoryDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.updateBookingHistory(bookHistoryId, bookingHistoryDTO)
        );
    }

//  Delete
    @Operation(
            summary = "Delete a specific booking history",
            description = "We will delete a specific booking history by providing Id",
            tags = {"Booking history", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @DeleteMapping("/deleteBookingHistory/{bookHistoryId}")
    public ResponseEntity<Map<String, String>> deleteBookingHistoryHandler(@PathVariable Integer bookHistoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookingHistoryService.deleteBookingHistory(bookHistoryId)
        );
    }
}
