package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.ServiceDTO;
import com.uit.hotelmanagement.entities.Service;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Service service = Service.builder()
                .serviceName(serviceDTO.getServiceName())
                .description(serviceDTO.getDescription())
                .price(serviceDTO.getPrice())
                .build();

        Service savedService =serviceRepository.save(service);

        return serviceToDTO(savedService);
    }

    @Override
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream().map(this::serviceToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceDTO getServiceById(Integer serviceId) {
        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));
        return serviceToDTO(existingService);
    }

    @Override
    public ServiceDTO getServiceByServiceName(String serviceName) {
        Service existingService = serviceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "service name", serviceName));
        return serviceToDTO(existingService);
    }

    @Override
    public List<ServiceDTO> getServiceByDescription(String description) {
        return serviceRepository.findByServiceNameContainingOrDescriptionContaining(description, description)
                .stream().map(this::serviceToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> getServiceByPriceBetween(String minPrice, String maxPrice) {
        return serviceRepository.findByPriceBetween(new BigDecimal(minPrice), new BigDecimal(maxPrice))
                .stream().map(this::serviceToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceDTO updateService(Integer serviceId, ServiceDTO serviceDTO) {
        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));

        existingService.setServiceName(serviceDTO.getServiceName());
        existingService.setDescription(serviceDTO.getDescription());
        existingService.setPrice(serviceDTO.getPrice());

        Service updatedService = serviceRepository.save(existingService);

        return serviceToDTO(updatedService);
    }

    @Override
    public Map<String, String> deleteService(Integer serviceId) {
        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));

        serviceRepository.delete(existingService);

        return Map.of("Message", "Service with Id " + serviceId + " has been deleted successfully!");
    }

    private ServiceDTO serviceToDTO(Service service) {
        return ServiceDTO.builder()
                .serviceId(service.getServiceId())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .price(service.getPrice())
                .build();
    }
}
