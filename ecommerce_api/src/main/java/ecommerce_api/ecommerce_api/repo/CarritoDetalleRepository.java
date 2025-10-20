package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {
    Optional<CarritoDetalle> findByCarrito_IdAndProducto_Id(Long carritoId, Long productoId);
    List<CarritoDetalle> findByCarrito_Id(Long carritoId);
}
