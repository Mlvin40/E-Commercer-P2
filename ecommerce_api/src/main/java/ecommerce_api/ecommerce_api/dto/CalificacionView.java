package ecommerce_api.ecommerce_api.dto;

import java.time.Instant;

/**
 * The type Calificacion view.
 */
public record CalificacionView(
        String usuario,
        Integer estrellas,
        String comentario,
        Instant fecha
) {}