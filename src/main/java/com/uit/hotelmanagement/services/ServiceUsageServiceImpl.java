package com.uit.hotelmanagement.services;

import com.uit.hotelmanagement.dtos.ServiceUsageDTO;
import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Service;
import com.uit.hotelmanagement.entities.ServiceUsage;
import com.uit.hotelmanagement.exceptions.BookingStatusException;
import com.uit.hotelmanagement.exceptions.CustomLocalDateException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import com.uit.hotelmanagement.repositories.BookingRepository;
import com.uit.hotelmanagement.repositories.ServiceRepository;
import com.uit.hotelmanagement.repositories.ServiceUsageRepository;
import com.uit.hotelmanagement.utils.BookingStatus;
import com.uit.hotelmanagement.utils.UtilityMethods;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceUsageServiceImpl implements ServiceUsageService{
    private final ServiceUsageRepository SURepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public ServiceUsageDTO createServiceUsage(Integer bookingId, Integer serviceId, ServiceUsageDTO serviceUsageDTO) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));

        // check bookingStatus
        BookingStatus bs = existingBooking.getBookingStatus();
        if (bs != BookingStatus.CONFIRMED && bs != BookingStatus.CHECKED_IN) {
            throw new BookingStatusException("This booking has not been confirmed or checked-in!");
        }

        // check startDate not after endDate
        LocalDate startDate = UtilityMethods.setLocalDate(serviceUsageDTO.getStartDate());
        LocalDate endDate = UtilityMethods.setLocalDate(serviceUsageDTO.getEndDate());
        if (startDate.isAfter(endDate)) {
            throw new CustomLocalDateException("The start date cannot be after the end date!");
        }

        // check startDate and endDate in a range
        if (startDate.isBefore(existingBooking.getCheckInDate()) || endDate.isAfter(existingBooking.getCheckOutDate())) {
            throw new CustomLocalDateException("Start and end dates cannot be outside of check-in and check-out times!");
        }

        ServiceUsage serviceUsage = ServiceUsage.builder()
                .numOfUsers(serviceUsageDTO.getNumOfUsers())
                .startDate(startDate)
                .endDate(endDate)
                .serviceVoucher(new BigDecimal(serviceUsageDTO.getServiceVoucher()))
                .service(existingService)
                .booking(existingBooking)
                .build();

        serviceUsage.setTotalPrice(serviceUsage.calculateTotalPrice());

        ServiceUsage savedServiceUsage = SURepository.save(serviceUsage);

        return serviceUsageToDTO(savedServiceUsage);
    }

    @Override
    public List<ServiceUsageDTO> getAllServiceUsage() {
        return SURepository.findAll().stream().map(this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public ServiceUsageDTO getServiceUsageById(Integer serviceUsageId) {
        ServiceUsage serviceUsage = SURepository.findById(serviceUsageId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceUsage", "serviceUsageId", Integer.toString(serviceUsageId)));
        return serviceUsageToDTO(serviceUsage);
    }

    @Override
    public List<ServiceUsageDTO> getAllByNumOfUsers(Integer numOfUsers) {
        return SURepository.findByNumOfUsers(numOfUsers).stream().map(this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByStartDate(String startDate) {
        return SURepository.findByStartDate(UtilityMethods.setLocalDate(startDate)).stream().map(
                this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByEndDate(String endDate) {
        return SURepository.findByEndDate(UtilityMethods.setLocalDate(endDate)).stream().map(
                this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByServiceVoucher(String serviceVoucher) {
        return SURepository.findByServiceVoucher(new BigDecimal(serviceVoucher)).stream().map(
                this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByPriceBetween(String minPrice, String maxPrice) {
        return SURepository.findByTotalPriceBetween(new BigDecimal(minPrice), new BigDecimal(maxPrice)).stream().map(
                this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByService(Integer serviceId) {
        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));

        return SURepository.findByService(existingService).stream().map(this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServiceUsageDTO> getAllByBooking(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        return SURepository.findByBooking(existingBooking).stream().map(this::serviceUsageToDTO).collect(Collectors.toList());
    }

    @Override
    public BigDecimal totalServicePriceOfBooking(Integer bookingId) {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        BigDecimal total = BigDecimal.ZERO;

        List<ServiceUsage> sus = existingBooking.getServiceUsages();

        for (ServiceUsage su : sus) {
            total = total.add(su.getTotalPrice());
        }

        return total;
    }

    @Override
    public ServiceUsageDTO updateServiceUsage(Integer serviceUsageId, Integer bookingId,
                                              Integer serviceId, ServiceUsageDTO serviceUsageDTO) {
        ServiceUsage existingServiceUsage = SURepository.findById(serviceUsageId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceUsage", "serviceUsageId", Integer.toString(serviceUsageId)));

        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", Integer.toString(bookingId)));

        Service existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId", Integer.toString(serviceId)));

        // check startDate not after endDate
        LocalDate startDate = UtilityMethods.setLocalDate(serviceUsageDTO.getStartDate());
        LocalDate endDate = UtilityMethods.setLocalDate(serviceUsageDTO.getEndDate());
        if (startDate.isAfter(endDate)) {
            throw new CustomLocalDateException("The start date cannot be after the end date!");
        }

        // check startDate and endDate in a range
        if (startDate.isBefore(existingBooking.getCheckInDate()) || endDate.isAfter(existingBooking.getCheckOutDate())) {
            throw new CustomLocalDateException("Start and end dates cannot be outside of check-in and check-out times!");
        }

        ServiceUsage newServiceUsage = ServiceUsage.builder()
                .serviceUsageId(existingServiceUsage.getServiceUsageId())
                .numOfUsers(serviceUsageDTO.getNumOfUsers())
                .startDate(startDate)
                .endDate(endDate)
                .serviceVoucher(new BigDecimal(serviceUsageDTO.getServiceVoucher()))
                .service(existingService)
                .booking(existingBooking)
                .build();

        newServiceUsage.setTotalPrice(newServiceUsage.calculateTotalPrice());

        ServiceUsage updatedServiceUsage = SURepository.save(newServiceUsage);

        return serviceUsageToDTO(updatedServiceUsage);
    }

    @Override
    public Map<String, String> deleteServiceUsage(Integer serviceUsageId) {
        ServiceUsage existingServiceUsage = SURepository.findById(serviceUsageId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceUsage", "serviceUsageId", Integer.toString(serviceUsageId)));

        SURepository.delete(existingServiceUsage);

        return Map.of("Message", "ServiceUsage with Id " + serviceUsageId + " has been deleted successfully!");
    }

    @Override
    public ServiceUsageDTO serviceUsageToDTO(ServiceUsage serviceUsage) {
        return ServiceUsageDTO.builder()
                .serviceUsageId(serviceUsage.getServiceUsageId())
                .numOfUsers(serviceUsage.getNumOfUsers())
                .startDate(serviceUsage.getStartDate().toString())
                .endDate(serviceUsage.getEndDate().toString())
                .serviceVoucher(serviceUsage.getServiceVoucher().toString())
                .totalPrice(serviceUsage.getTotalPrice().toString())
                .serviceName(serviceUsage.getService().getServiceName())
                .bookingId(serviceUsage.getBooking().getBookingId())
                .build();
    }
}
