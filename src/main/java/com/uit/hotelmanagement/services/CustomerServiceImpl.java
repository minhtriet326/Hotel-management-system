package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.CustomerDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Customer;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .email(customerDTO.getEmail())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .bookings(new ArrayList<>())
                .build();

        return customerToDTO(customerRepository.save(customer));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::customerToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Integer customerId) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", Integer.toString(customerId)));

        return customerToDTO(existingCustomer);
    }

    @Override
    public List<CustomerDTO> getAllCustomersByName(String name) {

        List<Customer> customers = customerRepository.findByFirstNameContainingOrLastNameContaining(name, name);

        return customers.stream().map(this::customerToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerByEmail(String email) {

        Customer existingCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        return customerToDTO(existingCustomer);
    }

    @Override
    public CustomerDTO getCustomerByPhoneNumber(String phoneNumber) {

        Customer existingCustomer = customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "phone number", phoneNumber));

        return customerToDTO(existingCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(Integer customerId, CustomerDTO customerDTO) {

        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", Integer.toString(customerId)));

        Customer updatedCustomer = Customer.builder()
                .customerId(customerId)
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .email(customerDTO.getEmail())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .bookings(existingCustomer.getBookings())
                .build();

        return customerToDTO(customerRepository.save(updatedCustomer));
    }

    @Override
    public Map<String, String> deleteCustomer(Integer customerId) {

        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", Integer.toString(customerId)));

        customerRepository.delete(existingCustomer);

        return Map.of("Message", "Customer with Id " + customerId + " has been deleted successfully!");
    }

    private CustomerDTO customerToDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .bookingIds(customer.getBookings().stream().map(Booking::getBookingId).collect(Collectors.toList()))
                .build();
    }
}
