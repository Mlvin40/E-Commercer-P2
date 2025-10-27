package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

/**
 * The type Top cliente ganancia row.
 */
public record TopClienteGananciaRow(
        Long clienteId,
        String nombre,
        String correo,
        BigDecimal totalGastado,   // suma(pe.total)
        BigDecimal gananciaSitio   // suma(pd.sitioFee)
) {}