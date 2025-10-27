package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The type Pedido logistica row.
 */
public record PedidoLogisticaRow(
        Long id,
        String comprador,
        BigDecimal total,
        LocalDate fechaPedido,
        LocalDate fechaEstimadaEntrega,
        String estado,
        int items
) {}
