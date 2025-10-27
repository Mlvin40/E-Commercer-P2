package ecommerce_api.ecommerce_api.dto;

/**
 * The type Rating create dto.
 */
public record RatingCreateDto(
        Long productoId,
        Integer estrellas,
        String comentario
) {}
