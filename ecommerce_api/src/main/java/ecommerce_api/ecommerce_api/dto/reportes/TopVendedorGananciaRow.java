package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

public record TopVendedorGananciaRow(
        Long vendedorId,
        String nombre,
        String correo,
        BigDecimal gananciaVendedor,  // suma(pd.gananciaVendedor)
        Long unidadesVendidas,        // suma(pd.cantidad)
        BigDecimal ventasBrutas       // suma(pd.subtotal)
) {}