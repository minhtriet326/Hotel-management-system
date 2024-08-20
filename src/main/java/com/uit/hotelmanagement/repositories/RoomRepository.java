package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Room;
import com.uit.hotelmanagement.utils.RoomStatus;
import com.uit.hotelmanagement.utils.RoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Optional<Room> findByRoomNumber(String roomNumber);
    Optional<List<Room>> findByRoomType(RoomType roomType);
    @Query("SELECT r FROM Room r WHERE r.price >= :min AND r.price <= :max")
    List<Room> findByPriceBetween(@Param("min") BigDecimal minPrice, @Param("max") BigDecimal maxPrice);
    Optional<List<Room>> findByRoomStatus(RoomStatus roomStatus);
}
