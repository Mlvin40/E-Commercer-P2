package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record CheckoutResponse(
        Long pedidoId,
        BigDecimal total,
        String estadoPago
) {}
