package com.uit.hotelmanagement.controllers;

import com.uit.hotelmanagement.dtos.ServiceDTO;
import com.uit.hotelmanagement.services.ServiceService;
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
@RequestMapping("/api/v1/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @Operation(
            summary = "Create a service",
            description = "We will create a service by providing a serviceDTO",
            tags = {"Service", "post"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/createService")
    public ResponseEntity<ServiceDTO> createServiceHandler(@Valid @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(serviceDTO));
    }

    @Operation(
            summary = "Get all services",
            description = "We will get all services",
            tags = {"Service", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getAllServices")
    public ResponseEntity<List<ServiceDTO>> getAllServicesHandler() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getAllServices());
    }

    @Operation(
            summary = "Get a specific service by Id",
            description = "We will get a specific service by providing Id",
            tags = {"Service", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getServiceById/{serviceId}")
    public ResponseEntity<ServiceDTO> getServiceByIdHandler(@PathVariable Integer serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceById(serviceId));
    }

    @Operation(
            summary = "Get a specific service by service name",
            description = "We will get a specific service by providing a serviceName",
            tags = {"Service", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getServiceByServiceName/{serviceName}")
    public ResponseEntity<ServiceDTO> getServiceByServiceNameHandler(@PathVariable String serviceName) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceByServiceName(serviceName));
    }

    @Operation(
            summary = "Get all services by description",
            description = "We will get all services that contain the keyword in its description by providing a keyword",
            tags = {"Service", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getServiceByDescription/{description}")
    public ResponseEntity<List<ServiceDTO>> getServiceByDescriptionHandler(@PathVariable String description) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceByDescription(description));
    }

    @Operation(
            summary = "Get all services by price",
            description = "We will get all services with prices between minPrice and maxPrice",
            tags = {"Service", "get"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = List.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/getServiceByPriceBetween")
    public ResponseEntity<List<ServiceDTO>> getServiceByPriceBetweenHandler(@RequestParam String minPrice,
                                                                            @RequestParam String maxPrice) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.getServiceByPriceBetween(minPrice, maxPrice));
    }

    @Operation(
            summary = "Update a specific service",
            description = "We will update a specific service by providing an Id and a serviceDTO",
            tags = {"Service", "put"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ServiceDTO.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PutMapping("/updateService/{serviceId}")
    public ResponseEntity<ServiceDTO> updateServiceHandler(@PathVariable Integer serviceId,
                                                           @Valid @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.updateService(serviceId, serviceDTO));
    }

    @Operation(
            summary = "Delete a specific service",
            description = "We will delete a specific service by providing Id",
            tags = {"Service", "delete"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @DeleteMapping("/deleteService/{serviceId}")
    public ResponseEntity<Map<String, String>> deleteServiceHandler(@PathVariable Integer serviceId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceService.deleteService(serviceId));
    }
}
