package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Optional<Service> findByServiceName(String serviceName);
    List<Service> findByServiceNameContainingOrDescriptionContaining(String keyword1, String keyword2);
    @Query("SELECT s FROM Service s WHERE s.price >= ?1 AND s.price <= ?2")
    List<Service> findByPriceBetween(@Param("min") BigDecimal minPrice, @Param("max") BigDecimal maxPrice);
}
