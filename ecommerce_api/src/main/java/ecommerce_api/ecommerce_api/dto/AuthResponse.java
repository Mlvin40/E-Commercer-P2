package ecommerce_api.ecommerce_api.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMs
) {}
