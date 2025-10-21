package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

public record TopVendedorUnidadesRow(
        Long vendedorId,
        String nombre,
        String correo,
        Long unidadesVendidas,   // sum(pd.cantidad)
        Long pedidos,            // count(distinct pe.id)
        BigDecimal ventasBrutas  // sum(pd.subtotal)
) {}