package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Booking;
import com.uit.hotelmanagement.entities.Service;
import com.uit.hotelmanagement.entities.ServiceUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ServiceUsageRepository extends JpaRepository<ServiceUsage, Integer> {
    List<ServiceUsage> findByNumOfUsers(Integer numberOfUsers);
    List<ServiceUsage> findByStartDate(LocalDate startDate);
    List<ServiceUsage> findByEndDate(LocalDate endDate);
    List<ServiceUsage> findByServiceVoucher(BigDecimal serviceVoucher);
    List<ServiceUsage> findByTotalPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<ServiceUsage> findByService(Service service);
    List<ServiceUsage> findByBooking(Booking booking);
}
