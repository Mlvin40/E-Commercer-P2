package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record CheckoutRequest(
        Long savedCardId,
        String numero,
        String expMes,
        String expAnio,
        String titular,
        Boolean guardar
) {}
