package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Catálogo público (solo aprobados)
    Page<Producto> findByEstadoPublicacion(String estadoPublicacion, Pageable pageable);

    // Catálogo con filtro por categoría
    Page<Producto> findByEstadoPublicacionAndCategoria(
            String estadoPublicacion, String categoria, Pageable pageable);

    // Búsqueda por nombre (texto) solo en aprobados
    Page<Producto> findByEstadoPublicacionAndNombreContainingIgnoreCase(
            String estadoPublicacion, String q, Pageable pageable);

    // Productos de un vendedor (para “mis productos”)
    List<Producto> findByVendedor_IdOrderByFechaPublicacionDesc(Long vendedorId);

    // (Opcional) Aprobados y con stock disponible
    Page<Producto> findByEstadoPublicacionAndStockGreaterThan(
            String estadoPublicacion, int stockMin, Pageable pageable);
}
