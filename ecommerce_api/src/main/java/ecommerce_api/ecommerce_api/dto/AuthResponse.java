package ecommerce_api.ecommerce_api.dto;

/**
 * The type Auth response.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInMs
) {}
