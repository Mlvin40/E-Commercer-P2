package ecommerce_api.ecommerce_api.dto;

/**
 * The type Calificacion request.
 */
public record CalificacionRequest(
        Long productoId,
        Integer estrellas,
        String comentario
) {}