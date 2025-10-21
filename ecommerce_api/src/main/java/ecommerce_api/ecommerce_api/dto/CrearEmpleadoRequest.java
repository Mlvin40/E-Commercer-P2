package ecommerce_api.ecommerce_api.dto;

import ecommerce_api.ecommerce_api.model.Role;
import jakarta.validation.constraints.*;


public record CrearEmpleadoRequest(
        @NotBlank String nombre,
        @NotBlank @Email String correo,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotNull Role rol     // MODERADOR | LOGISTICA | ADMIN
) {}