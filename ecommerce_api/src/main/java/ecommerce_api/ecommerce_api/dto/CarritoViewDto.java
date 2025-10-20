package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CarritoViewDto(
        Long carritoId,
        List<CarritoItemDto> items,
        BigDecimal subtotal
) {}
