package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record PedidoItemParaRatingDto(
        Long productoId,
        String nombre,
        String imagenUrl,
        Integer cantidad,
        BigDecimal precioUnitario,
        boolean yaCalificado
) {}
