package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Pedido repository.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Find by usuario id order by fecha pedido desc list.
     *
     * @param userId the user id
     * @return the list
     */
    List<Pedido> findByUsuario_IdOrderByFechaPedidoDesc(Long userId);

    /**
     * Find by estado page.
     *
     * @param estado   the estado
     * @param pageable the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = {"usuario", "detalles"}) // Esto evita la N+1 consulta que significa que carga usuario y detalles junto con pedido
    Page<Pedido> findByEstado(String estado, Pageable pageable);

    /**
     * Find by id and estado optional.
     *
     * @param id     the id
     * @param estado the estado
     * @return the optional
     */
    Optional<Pedido> findByIdAndEstado(Long id, String estado);
}
