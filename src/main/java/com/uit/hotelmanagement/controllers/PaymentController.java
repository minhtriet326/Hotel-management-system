package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.PaymentDTO;
import com.uit.hotelmanagement.services.PaymentService;
import com.uit.hotelmanagement.utils.records.PaymentRequest;
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
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

//  Post
    @Operation(
            summary = "Create a payment",
            description = "We will create a payment for a booking by providing bookingId and paymentDTO",
            tags = {"Payment", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class),
                                        mediaType = "application/json")
            )
    })
    @PostMapping("/createPayment/{bookingId}")
    public ResponseEntity<PaymentDTO> createPaymentHandler(@PathVariable Integer bookingId,
                                                           @Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.createPayment(bookingId, paymentRequest));
    }

//  Get
    @Operation(
            summary = "Get all payments",
            description = "We will get all payments in database",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllPayments")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPayments());
    }

    @Operation(
            summary = "Get a specific payment by Id",
            description = "We will get a specific payment by providing Id",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getPaymentById/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentByIdHandler(@PathVariable Integer paymentId) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentById(paymentId));
    }

    @Operation(
            summary = "Get all payments by payment date",
            description = "We will get all payments by providing paymentDate",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllPaymentsByPaymentDate")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByPaymentDateHandler(@RequestParam String paymentDate) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPaymentsByPaymentDate(paymentDate));
    }

    @Operation(
            summary = "Get all payments by amount",
            description = "We will get all payments with amounts between minAmount and maxAmount",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllPaymentsByAmountBetween")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByAmountBetweenHandler(@RequestParam String minAmount,
                                                                                 @RequestParam String maxAmount) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPaymentsByAmountBetween(minAmount, maxAmount));
    }

    @Operation(
            summary = "Get all payments by payment method",
            description = "We will get all payments by providing paymentMethodIndex",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllPaymentsByPaymentMethod/{paymentMethodIndex}")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByPaymentMethodHandler(@PathVariable String paymentMethodIndex) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPaymentsByPaymentMethod(paymentMethodIndex));
    }

    @Operation(
            summary = "Get all payments by payment status",
            description = "We will get all payments by providing paymentStatusIndex",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllPaymentsByPaymentStatus/{paymentStatusIndex}")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByPaymentStatusHandler(@PathVariable String paymentStatusIndex) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPaymentsByPaymentStatus(paymentStatusIndex));
    }

    @Operation(
            summary = "Get all payments by booking",
            description = "We will get all payments by providing bookingId",
            tags = {"Payment", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getPaymentByBooking/{bookingId}")
    public ResponseEntity<PaymentDTO> getPaymentByBookingHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentByBooking(bookingId));
    }

//  Put
    @Operation(
            summary = "Update a specific payment",
            description = "We will get update a specific payment by providing paymentId and paymentRequest",
            tags = {"Payment", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/updatePayment/{paymentId}")
    public ResponseEntity<PaymentDTO> updatePaymentHandler(@PathVariable Integer paymentId,
                                                           @Valid @RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.updatePayment(paymentId, paymentRequest));
    }

//  Delete
    @Operation(
            summary = "Delete a specific payment",
            description = "We will delete a specific payment by providing paymentId",
            tags = {"Payment", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @DeleteMapping("/deletePayment/{paymentId}")
    public ResponseEntity<Map<String, String>> deletePaymentHandler(@PathVariable Integer paymentId) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.deletePayment(paymentId));
    }
}
