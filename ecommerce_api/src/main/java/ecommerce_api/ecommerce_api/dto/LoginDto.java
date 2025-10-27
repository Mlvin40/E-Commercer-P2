package ecommerce_api.ecommerce_api.dto;

import jakarta.validation.constraints.*;

/**
 * The type Login dto.
 */
public record LoginDto(
        @Email @NotBlank String correo,
        @NotBlank String password
) {}
