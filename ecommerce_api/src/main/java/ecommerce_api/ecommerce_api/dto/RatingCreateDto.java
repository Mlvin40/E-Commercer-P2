package ecommerce_api.ecommerce_api.dto;

public record RatingCreateDto(
        Long productoId,
        Integer estrellas,
        String comentario
) {}
