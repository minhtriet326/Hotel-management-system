package com.uit.hotelmanagement.repositories;

import com.uit.hotelmanagement.entities.Photo;
import com.uit.hotelmanagement.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findByRoom(Room room);
    Optional<Photo> findByName(String name);
}
