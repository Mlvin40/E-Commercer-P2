package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

/**
 * The type Top vendedor unidades row.
 */
public record TopVendedorUnidadesRow(
        Long vendedorId,
        String nombre,
        String correo,
        Long unidadesVendidas,   // sum(pd.cantidad)
        Long pedidos,            // count(distinct pe.id)
        BigDecimal ventasBrutas  // sum(pd.subtotal)
) {}