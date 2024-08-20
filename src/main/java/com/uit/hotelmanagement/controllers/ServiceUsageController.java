package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.ServiceUsageDTO;
import com.uit.hotelmanagement.services.ServiceUsageService;
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
@RequestMapping("/api/v1/serviceUsage")
public class ServiceUsageController {
    private final ServiceUsageService sus;

//  Post
    @Operation(
            summary = "Create a specific ServiceUsage",
            description = "We will create a ServiceUsage by providing bookingId, serviceId and serviceUsageDTO",
            tags = {"Service usage", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ServiceUsageDTO.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/createServiceUsage/{bookingId}/{serviceId}")
    public ResponseEntity<ServiceUsageDTO> createServiceUsageHandler(@PathVariable Integer bookingId,
                                                                     @PathVariable Integer serviceId,
                                                                     @Valid @RequestBody ServiceUsageDTO serviceUsageDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.createServiceUsage(bookingId, serviceId, serviceUsageDTO));
    }

//  Get
    @Operation(
            summary = "Get all ServiceUsages",
            description = "We will get all ServiceUsages",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllServiceUsage")
    public ResponseEntity<List<ServiceUsageDTO>> getAllServiceUsage() {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllServiceUsage());
    }

    @Operation(
            summary = "Get a specific ServiceUsage by Id",
            description = "We will get a specific ServiceUsage by providing Id",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ServiceUsageDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getServiceUsageById/{serviceUsageId}")
    public ResponseEntity<ServiceUsageDTO> getServiceUsageByIdHandler(@PathVariable Integer serviceUsageId) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getServiceUsageById(serviceUsageId));
    }

    @Operation(
            summary = "Get all ServiceUsages by number of user",
            description = "We will get all ServiceUsages by providing number of user",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByNumOfUsers/{numOfUsers}")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByNumOfUsersHandler(@PathVariable Integer numOfUsers) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByNumOfUsers(numOfUsers));
    }

    @Operation(
            summary = "Get all ServiceUsages by start date",
            description = "We will get all ServiceUsages by providing startDate",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByStartDate")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByStartDateHandler(@RequestParam String startDate) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByStartDate(startDate));
    }

    @Operation(
            summary = "Get all ServiceUsages by end date",
            description = "We will get all ServiceUsages by providing endDate",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByEndDate")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByEndDateHandler(@RequestParam String endDate) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByEndDate(endDate));
    }

    @Operation(
            summary = "Get all ServiceUsages by service voucher",
            description = "We will get all ServiceUsages by providing serviceVoucher",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByServiceVoucher/{serviceVoucher}")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByServiceVoucherHandler(@PathVariable String serviceVoucher) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByServiceVoucher(serviceVoucher));
    }

    @Operation(
            summary = "Get all ServiceUsages by total price",
            description = "We will get all ServiceUsages with total price between minPrice and maxPrice",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByPriceBetween")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByPriceBetweenHandler(@RequestParam String minPrice,
                                                                             @RequestParam String maxPrice) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByPriceBetween(minPrice, maxPrice));
    }

    @Operation(
            summary = "Get all ServiceUsages by service",
            description = "We will get all ServiceUsages by providing serviceId",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByService/{serviceId}")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByServiceHandler(@PathVariable Integer serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByService(serviceId));
    }

    @Operation(
            summary = "Get all ServiceUsages by booking",
            description = "We will get all ServiceUsages by providing bookingId",
            tags = {"Service usage", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllByBooking/{bookingId}")
    public ResponseEntity<List<ServiceUsageDTO>> getAllByBookingHandler(@PathVariable Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.getAllByBooking(bookingId));
    }

//  Put
    @Operation(
            summary = "Update a specific ServiceUsages",
            description = "We will update a specific ServiceUsage by providing serviceUsageId, bookingId, serviceId and serviceUsageDTO",
            tags = {"Service usage", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ServiceUsageDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/updateServiceUsage")
    public ResponseEntity<ServiceUsageDTO> updateServiceUsageHandler(@RequestParam Integer serviceUsageId,
                                                                     @RequestParam Integer bookingId,
                                                                     @RequestParam Integer serviceId,
                                                                     @Valid @RequestBody ServiceUsageDTO serviceUsageDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.updateServiceUsage(serviceUsageId, bookingId, serviceId, serviceUsageDTO));
    }

//  Delete
    @Operation(
            summary = "Delete a specific ServiceUsage",
            description = "We will delete a specific ServiceUsage by providing Id",
            tags = {"Service usage", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @DeleteMapping("/deleteServiceUsage/{serviceUsageId}")
    public ResponseEntity<Map<String, String>> deleteServiceUsageHandler(@PathVariable Integer serviceUsageId) {
        return ResponseEntity.status(HttpStatus.OK).body(sus.deleteServiceUsage(serviceUsageId));
    }
}
