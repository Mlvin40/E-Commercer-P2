package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

/**
 * The type Checkout response.
 */
public record CheckoutResponse(
        Long pedidoId,
        BigDecimal total,
        String estadoPago
) {}
