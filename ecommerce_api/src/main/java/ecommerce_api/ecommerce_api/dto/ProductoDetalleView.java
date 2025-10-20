package ecommerce_api.ecommerce_api.dto;

import java.math.BigDecimal;

public record ProductoDetalleView(
        Long id,
        String nombre,
        String descripcion,
        String imagenUrl,
        BigDecimal precio,
        Integer stock,
        String estado,
        String categoria,
        String vendedorNombre
) {}
