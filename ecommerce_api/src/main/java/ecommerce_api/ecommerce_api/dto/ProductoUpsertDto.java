package ecommerce_api.ecommerce_api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoUpsertDto(
        @NotBlank String nombre,
        String descripcion,
        String imagenUrl,
        @NotNull @DecimalMin("0.01") BigDecimal precio,
        @NotNull @Min(1) Integer stock,
        @NotBlank String estado,
        @NotBlank String categoria
) {}
