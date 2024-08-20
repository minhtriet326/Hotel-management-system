package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.ServiceUsageDTO;
import com.uit.hotelmanagement.entities.ServiceUsage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ServiceUsageService {
    // Post
    ServiceUsageDTO createServiceUsage(Integer bookingId, Integer serviceId, ServiceUsageDTO serviceUsageDTO);

    // Get
    List<ServiceUsageDTO> getAllServiceUsage();
    ServiceUsageDTO getServiceUsageById(Integer serviceUsageId);
    List<ServiceUsageDTO> getAllByNumOfUsers(Integer numOfUsers);
    List<ServiceUsageDTO> getAllByStartDate(String startDate);
    List<ServiceUsageDTO> getAllByEndDate(String endDate);
    List<ServiceUsageDTO> getAllByServiceVoucher(String serviceVoucher);
    List<ServiceUsageDTO> getAllByPriceBetween(String minPrice, String maxPrice);
    List<ServiceUsageDTO> getAllByService(Integer serviceId);
    List<ServiceUsageDTO> getAllByBooking(Integer bookingId);
    BigDecimal totalServicePriceOfBooking(Integer bookingId);
    // Put
    ServiceUsageDTO updateServiceUsage(Integer serviceUsageId, Integer bookingId,
                                       Integer serviceId, ServiceUsageDTO serviceUsageDTO);

    // Delete
    Map<String, String> deleteServiceUsage(Integer serviceUsageId);

    // Features
    ServiceUsageDTO serviceUsageToDTO(ServiceUsage serviceUsage);
}
