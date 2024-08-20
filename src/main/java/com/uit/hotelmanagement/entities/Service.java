package com.uit.hotelmanagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "services")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;

    @NotBlank(message = "Service's name can't be blank")
    @Column(unique = true)
    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @PositiveOrZero(message = "Service price cannot be negative")
    @Column(precision = 19, scale = 10)
    private BigDecimal price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceUsage> serviceUsages;
}
