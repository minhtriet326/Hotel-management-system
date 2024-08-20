package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.CustomerDTO;
import com.uit.hotelmanagement.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Create customer",
            description = "We will create a customer by providing the customerDTO",
            tags = {"Customer", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class),
                                        mediaType = "application/json")
            )
    })
    @PostMapping("/createCustomer")
    public ResponseEntity<CustomerDTO> createCustomerHandler(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDTO));
    }

    @Operation(
            summary = "Get all customers",
            description = "We will get all customer",
            tags = {"Customer", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
    }

    @Operation(
            summary = "Get a specific customer by Id",
            description = "We will get a specific customer by providing Id",
            tags = {"Customer", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getCustomerById/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerByIdHandler(@PathVariable Integer customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(customerId));
    }

    @Operation(
            summary = "Get all customers by name",
            description = "We will get all customer that contain the keyword in their firstname or lastname by providing a name",
            tags = {"Customer", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getAllCustomersByName/{name}")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersByNameHandler(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomersByName(name));
    }

    @Operation(
            summary = "Get a specific customer by email",
            description = "We will get a specific customer by providing an email",
            tags = {"Customer", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getCustomerByEmail/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmailHandler(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerByEmail(email));
    }

    @Operation(
            summary = "Get a specific customer by phone number",
            description = "We will get a specific customer by providing a phone number",
            tags = {"Customer", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class),
                            mediaType = "application/json")
            )
    })
    @GetMapping("/getCustomerByPhoneNumber/{phoneNumber}")
    public ResponseEntity<CustomerDTO> getCustomerByPhoneNumberHandler(@PathVariable String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerByPhoneNumber(phoneNumber));
    }

    @Operation(
            summary = "Update a specific customer",
            description = "We will update a specific customer by providing Id and customerDTO",
            tags = {"Customer", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class),
                            mediaType = "application/json")
            )
    })
    @PutMapping("/updateCustomer/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomerHandler(@PathVariable Integer customerId,
                                                             @Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(customerId, customerDTO));
    }

    @Operation(
            summary = "Delete a specific customer",
            description = "We will delete a specific customer by providing Id",
            tags = {"Customer", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = "application/json")
            )
    })
    @DeleteMapping("/deleteCustomer/{customerId}")
    public ResponseEntity<Map<String, String>> deleteCustomerHandler(@PathVariable Integer customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.deleteCustomer(customerId));
    }
}
