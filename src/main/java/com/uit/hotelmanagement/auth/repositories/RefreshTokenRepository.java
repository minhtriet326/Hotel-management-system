package com.uit.hotelmanagement.auth.repositories;

import com.uit.hotelmanagement.auth.entities.RefreshToken;
import com.uit.hotelmanagement.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    RefreshToken findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
