package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.ServiceDTO;

import java.util.List;
import java.util.Map;

public interface ServiceService {
    // Post
    ServiceDTO createService(ServiceDTO serviceDTO);
    // Get
    List<ServiceDTO> getAllServices();
    ServiceDTO getServiceById(Integer serviceId);
    ServiceDTO getServiceByServiceName(String serviceName);
    List<ServiceDTO> getServiceByDescription(String description);
    List<ServiceDTO> getServiceByPriceBetween(String minPrice, String maxPrice);
    // Put
    ServiceDTO updateService(Integer serviceId, ServiceDTO serviceDTO);
    // Delete
    Map<String, String> deleteService(Integer serviceId);

}
