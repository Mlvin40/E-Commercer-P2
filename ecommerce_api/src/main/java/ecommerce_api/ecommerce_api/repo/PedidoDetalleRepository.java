package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    @Query("select count(pd) > 0 from PedidoDetalle pd " +
            "where pd.pedido.usuario.id = :u and pd.producto.id = :p")
    boolean userBoughtProduct(@Param("u") Long userId, @Param("p") Long productoId);

    // para el rating
    List<PedidoDetalle> findByPedido_Id(Long pedidoId);
}
