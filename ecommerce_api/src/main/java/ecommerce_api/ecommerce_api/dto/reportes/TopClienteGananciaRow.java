package ecommerce_api.ecommerce_api.dto.reportes;

import java.math.BigDecimal;

public record TopClienteGananciaRow(
        Long clienteId,
        String nombre,
        String correo,
        BigDecimal totalGastado,   // suma(pe.total)
        BigDecimal gananciaSitio   // suma(pd.sitioFee)
) {}