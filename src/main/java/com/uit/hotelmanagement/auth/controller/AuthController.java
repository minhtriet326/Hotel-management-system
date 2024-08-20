package com.uit.hotelmanagement.auth.controller;

import com.uit.hotelmanagement.auth.entities.RefreshToken;
import com.uit.hotelmanagement.auth.entities.User;
import com.uit.hotelmanagement.auth.services.AuthService;
import com.uit.hotelmanagement.auth.services.JwtService;
import com.uit.hotelmanagement.auth.services.RefreshTokenService;
import com.uit.hotelmanagement.auth.utils.records.AuthResponse;
import com.uit.hotelmanagement.auth.utils.records.LoginRequest;
import com.uit.hotelmanagement.auth.utils.records.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Operation(
            summary = "Register",
            description = "We will register by providing username and password",
            tags = {"Auth"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody RegisterRequest registerRequest)
                                                            throws SQLIntegrityConstraintViolationException {
        return ResponseEntity.status(HttpStatus.OK).body(authService.Register(registerRequest));
    }

    @Operation(
            summary = "Login",
            description = "We will login by providing username and password",
            tags = {"Auth"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.Login(loginRequest));
    }

    @Operation(
            summary = "Refresh access token",
            description = "We will refresh access token by providing the refreshToken",
            tags = {"Auth"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping("/refreshAccessToken") // lấy accessToken mới khi accessToken cũ hết hạn
    public ResponseEntity<AuthResponse> refreshAccessTokenHandler(String refreshToken) {
        // check for refreshToken has expired?
        RefreshToken usefulRefreshToken = refreshTokenService.verifyRefreshToken(refreshToken);

        User existingUser = usefulRefreshToken.getUser();

        String newAccessToken = jwtService.generateToken(existingUser);

        return ResponseEntity.status(HttpStatus.OK).body(AuthResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(newAccessToken)
                .build());
    }
}
