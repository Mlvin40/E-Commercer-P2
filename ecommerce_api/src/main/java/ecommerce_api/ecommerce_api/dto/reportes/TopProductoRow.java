package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

/**
 * The type Top producto row.
 */
public record TopProductoRow(
        Long productoId,
        String nombre,
        Long totalUnidades,
        BigDecimal totalVentas
) {}