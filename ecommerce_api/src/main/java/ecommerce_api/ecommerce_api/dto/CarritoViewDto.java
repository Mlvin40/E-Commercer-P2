package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Carrito view dto.
 */
public record CarritoViewDto(
        Long carritoId,
        List<CarritoItemDto> items,
        BigDecimal subtotal
) {}
