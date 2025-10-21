package ecommerce_api.ecommerce_api.dto.reportes;

import java.time.LocalDate;

public record SancionHistRow(
        Long id,
        Long moderadorId,
        String moderador,
        Long usuarioId,
        String usuario,
        String correo,
        String motivo,
        LocalDate fecha,
        String estado
) {}