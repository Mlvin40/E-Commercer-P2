package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.ProductoRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRevisionRepository extends JpaRepository<ProductoRevision, Long> {
    List<ProductoRevision> findByEstadoOrderByCreadoEnAsc(String estado);
    boolean existsByProductoAndEstado(Producto p, String estado);



}
