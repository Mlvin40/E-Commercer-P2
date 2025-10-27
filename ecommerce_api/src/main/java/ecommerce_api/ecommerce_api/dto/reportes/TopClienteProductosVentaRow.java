package ecommerce_api.ecommerce_api.dto.reportes;

/**
 * The type Top cliente productos venta row.
 */
public record TopClienteProductosVentaRow(
        Long vendedorId,
        String nombre,
        String correo,
        Long productosEnVenta,   // count(p.id)
        Long stockTotal          // sum(p.stock)
) {}