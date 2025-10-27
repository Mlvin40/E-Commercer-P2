package ecommerce_api.ecommerce_api.dto;

import java.time.LocalDate;

/**
 * The type Sancion view.
 */
public record SancionView(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        Long moderadorId,
        String moderadorNombre,
        String motivo,
        LocalDate fecha,
        String estado
) { }