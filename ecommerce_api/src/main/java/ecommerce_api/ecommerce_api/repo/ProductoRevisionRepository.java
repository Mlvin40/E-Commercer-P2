package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.ProductoRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRevisionRepository extends JpaRepository<ProductoRevision, Long> {
    List<ProductoRevision> findByEstadoOrderByCreadoEnAsc(String estado);

    boolean existsByProductoAndEstado(Producto p, String estado);

    // NUEVO: trae la revisión pendiente de un producto (si existe)
    Optional<ProductoRevision> findFirstByProducto_IdAndEstadoOrderByCreadoEnAsc(
            Long productoId, String estado);
}
