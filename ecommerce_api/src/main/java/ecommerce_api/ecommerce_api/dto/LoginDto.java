package ecommerce_api.ecommerce_api.dto;

import jakarta.validation.constraints.*;

public record LoginDto(
        @Email @NotBlank String correo,
        @NotBlank String password
) {}
