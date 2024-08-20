package com.uit.hotelmanagement.auth.services;

import com.uit.hotelmanagement.auth.entities.RefreshToken;
import com.uit.hotelmanagement.auth.entities.User;
import com.uit.hotelmanagement.auth.repositories.RefreshTokenRepository;
import com.uit.hotelmanagement.auth.repositories.UserRepository;
import com.uit.hotelmanagement.exceptions.RefreshTokenException;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    // create
    public RefreshToken createRefreshToken(String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (existingUser.getRefreshToken() == null) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .user(existingUser)
                    .build();

            return refreshTokenRepository.save(newRefreshToken);
        }

        return existingUser.getRefreshToken();
    }
    // check token has expired
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken existingToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "refreshToken", refreshToken));

        if (existingToken.getExpirationDate().before(new Date())) {
            throw new RefreshTokenException("Refresh token has expired!");
        }

        return existingToken;
    }
}
