package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuario_Id(Long usuarioId);
}
