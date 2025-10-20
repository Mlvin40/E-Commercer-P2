package ecommerce_api.ecommerce_api.dto;

public record CalificacionRequest(
        Long productoId,
        Integer estrellas,
        String comentario
) {}