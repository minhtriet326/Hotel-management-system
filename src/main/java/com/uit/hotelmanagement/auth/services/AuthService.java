package com.uit.hotelmanagement.auth.services;

import com.uit.hotelmanagement.auth.entities.RefreshToken;
import com.uit.hotelmanagement.auth.entities.User;
import com.uit.hotelmanagement.auth.repositories.UserRepository;
import com.uit.hotelmanagement.auth.utils.records.AuthResponse;
import com.uit.hotelmanagement.auth.utils.records.LoginRequest;
import com.uit.hotelmanagement.auth.utils.records.RegisterRequest;
import com.uit.hotelmanagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public AuthResponse Login(LoginRequest loginRequest) {
        User existingUser = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", loginRequest.username()));

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.username(),
                    loginRequest.password()
            );

            authenticationManager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password!");
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.username());

        String accessToken = jwtService.generateToken(existingUser);

        return AuthResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }
    public AuthResponse Register(RegisterRequest registerRequest) throws SQLIntegrityConstraintViolationException {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new SQLIntegrityConstraintViolationException("The username is already in use!");
        }

        User newUser = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles("USER")
                .build();

        User savedUser = userRepository.save(newUser);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(registerRequest.username());

        String accessToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .refreshToken(newRefreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }
}
