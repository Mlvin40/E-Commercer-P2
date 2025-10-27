package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

/**
 * The type Carrito item dto.
 */
public record CarritoItemDto(
        Long productoId,
        String nombre,
        String imagenUrl,
        BigDecimal precio,
        Integer cantidad,
        Integer stockDisponible
) {}
