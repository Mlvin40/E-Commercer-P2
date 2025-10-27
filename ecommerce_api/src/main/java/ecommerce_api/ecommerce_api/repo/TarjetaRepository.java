package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Tarjeta repository.
 */
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    /**
     * Find by usuario id order by fecha registro desc list.
     *
     * @param usuarioId the usuario id
     * @return the list
     */
    List<Tarjeta> findByUsuario_IdOrderByFechaRegistroDesc(Long usuarioId);
}