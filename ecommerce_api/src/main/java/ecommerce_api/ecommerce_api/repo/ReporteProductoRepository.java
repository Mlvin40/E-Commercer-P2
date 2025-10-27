// src/main/java/ecommerce_api/ecommerce_api/repo/ReporteProductoRepository.java
package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.reportes.TopClienteProductosVentaRow;
import ecommerce_api.ecommerce_api.model.Producto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * The interface Reporte producto repository.
 */
public interface ReporteProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Top clientes con mas productos en venta list.
     *
     * @param pageable the pageable
     * @return the list
     */
    @Query("""
        select new ecommerce_api.ecommerce_api.dto.reportes.TopClienteProductosVentaRow(
            u.id, u.nombre, u.correo,
            count(p.id),
            coalesce(sum(p.stock), 0)
        )
        from Producto p
            join p.vendedor u
        where p.estadoPublicacion = 'APROBADO'
          and p.stock > 0
        group by u.id, u.nombre, u.correo
        order by count(p.id) desc, sum(p.stock) desc
        """)
    List<TopClienteProductosVentaRow> topClientesConMasProductosEnVenta(Pageable pageable);
}
