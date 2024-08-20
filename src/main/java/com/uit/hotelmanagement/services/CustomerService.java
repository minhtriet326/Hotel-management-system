package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.CustomerDTO;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    // Post
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    // Get
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Integer customerId);
    List<CustomerDTO> getAllCustomersByName(String name);
    CustomerDTO getCustomerByEmail(String email);
    CustomerDTO getCustomerByPhoneNumber(String phoneNumber);

    // Put
    CustomerDTO updateCustomer(Integer customerId, CustomerDTO customerDTO);

    // Delete
    Map<String, String> deleteCustomer(Integer customerId);
}

