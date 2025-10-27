package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The type Pedido list item dto.
 */
public record PedidoListItemDto(
        Long id,
        LocalDate fecha,
        LocalDate fechaEstimadaEntrega,
        String estado,
        BigDecimal total
) {}
