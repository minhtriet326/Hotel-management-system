package com.uit.hotelmanagement.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerDTO {
    private Integer customerId;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @Email(message = "Please enter a proper email")
    private String email;

    @NotBlank(message = "Phone number can't be blank")
    @Size(min = 7, max = 15, message = "Phone number must be at least 7 characters and maximum 15 characters")
    private String phoneNumber;

    private String address;

    private List<Integer> bookingIds;
}
