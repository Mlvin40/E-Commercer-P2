package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

public record TopProductoRow(
        Long productoId,
        String nombre,
        Long totalUnidades,
        BigDecimal totalVentas
) {}