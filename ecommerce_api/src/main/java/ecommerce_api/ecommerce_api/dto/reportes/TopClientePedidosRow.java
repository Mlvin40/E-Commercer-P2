package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

/**
 * The type Top cliente pedidos row.
 */
public record TopClientePedidosRow(
        Long clienteId,
        String nombre,
        String correo,
        Long pedidos,            // count(pe.id)
        BigDecimal totalGastado  // sum(pe.total)
) {}
