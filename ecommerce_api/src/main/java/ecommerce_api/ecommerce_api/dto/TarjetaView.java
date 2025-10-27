package ecommerce_api.ecommerce_api.dto;

/**
 * The type Tarjeta view.
 */
public record TarjetaView(
        Long id,
        String last4,
        String marca,
        String titular,
        boolean predeterminada
) {}
