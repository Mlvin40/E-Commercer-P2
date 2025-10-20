package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario_IdOrderByFechaPedidoDesc(Long userId);
}
