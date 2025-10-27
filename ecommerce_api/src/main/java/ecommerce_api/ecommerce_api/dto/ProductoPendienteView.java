package ecommerce_api.ecommerce_api.dto;

import java.time.Instant;

/**
 * The type Producto pendiente view.
 */
public record ProductoPendienteView(
        Long productoId,
        String nombre,
        String categoria,
        String estado,
        String imagenUrl,
        String vendedorNombre,
        String vendedorCorreo,
        Instant creadoEn
) {}
