package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario_IdOrderByFechaPedidoDesc(Long userId);

    @EntityGraph(attributePaths = {"usuario", "detalles"}) // evitá N+1
    Page<Pedido> findByEstado(String estado, Pageable pageable);

    Optional<Pedido> findByIdAndEstado(Long id, String estado);
}
