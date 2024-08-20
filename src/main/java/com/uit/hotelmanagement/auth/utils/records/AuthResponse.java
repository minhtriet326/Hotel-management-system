package com.uit.hotelmanagement.auth.utils.records;

import lombok.Builder;

@Builder
public record AuthResponse(String refreshToken, String accessToken) {
}
