package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record ProductoCardView(
        Long id,
        String nombre,
        String imagenUrl,
        BigDecimal precio,
        Integer stock,
        String categoria,
        String vendedorNombre,
        String vendedorCorreo,
        Double avgRating,
        Long ratingsCount
) {}