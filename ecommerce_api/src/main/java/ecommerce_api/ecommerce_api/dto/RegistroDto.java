package ecommerce_api.ecommerce_api.dto;
import jakarta.validation.constraints.*;

public record RegistroDto(
        @NotBlank String nombre,
        @Email @NotBlank String correo,
        @NotBlank @Size(min = 6) String password
) {}
