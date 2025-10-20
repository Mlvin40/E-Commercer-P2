package ecommerce_api.ecommerce_api.dto;

public record TarjetaView(
        Long id,
        String last4,
        String marca,
        String titular,
        boolean predeterminada
) {}
