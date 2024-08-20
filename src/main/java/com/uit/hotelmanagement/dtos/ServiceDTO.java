package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServiceDTO {
    private Integer serviceId;

    @NotBlank(message = "Service's name can't be blank")
    private String serviceName;

    private String description;

    @PositiveOrZero(message = "Service price cannot be negative")
    private BigDecimal price;
}
