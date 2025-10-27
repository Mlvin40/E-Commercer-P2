package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

/**
 * The type Pedido item para rating dto.
 */
public record PedidoItemParaRatingDto(
        Long productoId,
        String nombre,
        String imagenUrl,
        Integer cantidad,
        BigDecimal precioUnitario,
        boolean yaCalificado
) {}
