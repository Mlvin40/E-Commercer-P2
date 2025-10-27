package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Pedido detalle repository.
 */
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    /**
     * User bought product boolean.
     *
     * @param userId     the user id
     * @param productoId the producto id
     * @return the boolean
     */
    @Query("select count(pd) > 0 from PedidoDetalle pd " +
            "where pd.pedido.usuario.id = :u and pd.producto.id = :p")
    boolean userBoughtProduct(@Param("u") Long userId, @Param("p") Long productoId);

    /**
     * Find by pedido id list.
     *
     * @param pedidoId the pedido id
     * @return the list
     */
// para el rating
    List<PedidoDetalle> findByPedido_Id(Long pedidoId);
}
