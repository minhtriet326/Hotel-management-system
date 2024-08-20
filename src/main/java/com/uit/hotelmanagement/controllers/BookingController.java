package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.BookingDTO;
import com.uit.hotelmanagement.services.BookingService;
import com.uit.hotelmanagement.utils.records.ChangeRoomRequest;
import com.uit.hotelmanagement.utils.records.CheckInRequest;
import com.uit.hotelmanagement.utils.records.CheckOutOrExtendRequest;
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
@RequestMapping("api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

// Post
    @Operation(
            summary = "Reservation",
            description = "We will book a room by providing customerId, roomId and bookingDTO",
            tags = {"Booking", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/reserveRoom/{customerId}/{roomId}")
    public ResponseEntity<BookingDTO> reserveRoomHandler(@PathVariable Integer customerId,
                                                         @PathVariable Integer roomId,
                                                         @Valid @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.reserveRoom(customerId, roomId, bookingDTO));
    }

// Get
    @Operation(
            summary = "Get all bookings",
            description = "We will get all bookings in database",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookings")
    public ResponseEntity<List<BookingDTO>> getAllBookingsHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings());
    }

    @Operation(
            summary = "Get a specific booking by Id",
            description = "We will get a specific booking by providing Id",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getBookingById/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingByIdHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingById(bookingId));
    }

    @Operation(
            summary = "Get all bookings by check-in date",
            description = "We will get all bookings with the same check-in date",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingsByCheckInDate")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByCheckInDateHandler(@RequestParam String checkInDate) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingsByCheckInDate(checkInDate));
    }

    @Operation(
            summary = "Get all bookings by check-out date",
            description = "We will get all bookings with the same check-out date",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingsByCheckOutDate")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByCheckOutDateHandler(@RequestParam String checkOutDate) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingsByCheckOutDate(checkOutDate));
    }

    @Operation(
            summary = "Get all bookings by booking status",
            description = "We will get all bookings with the same booking status",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingsByBookingStatus/{bookingStatusIndex}")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByBookingStatusHandler(@PathVariable String bookingStatusIndex) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingsByBookingStatus(bookingStatusIndex));
    }

    @Operation(
            summary = "Get all bookings by customer",
            description = "We will get all bookings of a specific customer",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingsByCustomer/{customerId}")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByCustomerHandler(@PathVariable Integer customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingsByCustomer(customerId));
    }

    @Operation(
            summary = "Get all bookings by room",
            description = "We will get all bookings related to a specific room",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllBookingsByRoom/{roomId}")
    public ResponseEntity<List<BookingDTO>> getAllBookingsByRoomHandler(@PathVariable Integer roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingsByRoom(roomId));
    }

    @Operation(
            summary = "Get current total price of a booking",
            description = "We will get the current total price of a booking",
            tags = {"Booking", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/currentTotalPrice/{bookingId}")
    public ResponseEntity<Map<String, String>> currentTotalPriceHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("Total price", bookingService.finalTotalPrice(bookingId).toString()));
    }

// Put
    @Operation(
            summary = "Update a specific booking",
            description = "We will update a specific booking by providing bookingId, customerId, roomId and bookingDTO",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/updateBooking/{bookingId}/{roomId}")
    public ResponseEntity<BookingDTO> updateBookingHandler(@PathVariable Integer bookingId,
                                                           @PathVariable Integer roomId,
                                                           @Valid @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingId, roomId, bookingDTO));
    }

    @Operation(
            summary = "Check in",
            description = "We will check in by providing bookingId and checkInRequest",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/checkIn/{bookingId}")
    public ResponseEntity<BookingDTO> checkInHandler(@PathVariable Integer bookingId,
                                                     @Valid @RequestBody CheckInRequest checkInRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.checkIn(bookingId, checkInRequest));
    }

    @Operation(
            summary = "Check out",
            description = "We will check out by providing bookingId and checkOutRequest",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/checkOut/{bookingId}")
    public ResponseEntity<BookingDTO> checkOutHandler(@PathVariable Integer bookingId,
                                                      @Valid @RequestBody CheckOutOrExtendRequest checkOutRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.checkOut(bookingId, checkOutRequest));
    }

    @Operation(
            summary = "Extend stay",
            description = "We will extend stay by providing bookingId and extendStayRequest",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/extendStay/{bookingId}")
    public ResponseEntity<BookingDTO> extendStayHandler(@PathVariable Integer bookingId,
                                                        @Valid @RequestBody CheckOutOrExtendRequest extendStayRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.extendStay(bookingId, extendStayRequest));
    }

    @Operation(
            summary = "Change room",
            description = "We will change room for a booking by providing bookingId, newRoomId and change reason",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/changeRoom/{bookingId}/{newRoomId}")
    public ResponseEntity<BookingDTO> changeRoomHandler(@PathVariable Integer bookingId,
                                                        @PathVariable Integer newRoomId,
                                                        @Valid @RequestBody ChangeRoomRequest changeRoomRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.changeRoom(bookingId, newRoomId, changeRoomRequest));
    }

    @Operation(
            summary = "Cancel a specific booking",
            description = "We will cancel a specific booking by providing bookingId and note",
            tags = {"Booking", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BookingDTO.class),
                                        mediaType = "application/json")
            )
    })
    @PutMapping("/cancelBooking")
    public ResponseEntity<BookingDTO> cancelBookingHandler(@RequestParam Integer bookingId,
                                                           @RequestParam(required = false) String note) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.cancelBooking(bookingId, note));
    }

// Delete
    @Operation(
            summary = "Delete a specific booking",
            description = "We will delete a specific booking by providing bookingId",
            tags = {"Booking", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @DeleteMapping("/deleteBooking/{bookingId}")
    public ResponseEntity<Map<String, String>> deleteBookingHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.deleteBooking(bookingId));
    }
}
