package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * The interface Carrito repository.
 */
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    /**
     * Find by usuario id optional.
     *
     * @param usuarioId the usuario id
     * @return the optional
     */
    Optional<Carrito> findByUsuario_Id(Long usuarioId);
}
