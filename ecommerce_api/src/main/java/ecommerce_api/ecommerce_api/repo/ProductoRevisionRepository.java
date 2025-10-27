package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.ProductoRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Producto revision repository.
 */
public interface ProductoRevisionRepository extends JpaRepository<ProductoRevision, Long> {
    /**
     * Find by estado order by creado en asc list.
     *
     * @param estado the estado
     * @return the list
     */
    List<ProductoRevision> findByEstadoOrderByCreadoEnAsc(String estado);

    /**
     * Exists by producto and estado boolean.
     *
     * @param p      the p
     * @param estado the estado
     * @return the boolean
     */
    boolean existsByProductoAndEstado(Producto p, String estado);

    /**
     * Find first by producto id and estado order by creado en asc optional.
     *
     * @param productoId the producto id
     * @param estado     the estado
     * @return the optional
     */
  //trae la revisión pendiente de un producto. si existe
    Optional<ProductoRevision> findFirstByProducto_IdAndEstadoOrderByCreadoEnAsc(
            Long productoId, String estado);
}
