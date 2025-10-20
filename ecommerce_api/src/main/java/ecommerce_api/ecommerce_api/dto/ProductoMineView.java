package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductoMineView(
        Long id,
        String nombre,
        String descripcion,
        String imagenUrl,
        BigDecimal precio,
        Integer stock,
        String estado,
        String categoria,
        String estadoPublicacion,
        Instant fechaPublicacion
) {}
