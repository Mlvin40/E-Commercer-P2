package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.CarritoDetalle;
import ecommerce_api.ecommerce_api.model.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Carrito detalle repository.
 */
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Long> {
    /**
     * Find by carrito id and producto id optional.
     *
     * @param carritoId  the carrito id
     * @param productoId the producto id
     * @return the optional
     */
    Optional<CarritoDetalle> findByCarrito_IdAndProducto_Id(Long carritoId, Long productoId);

    /**
     * Find by carrito id list.
     *
     * @param carritoId the carrito id
     * @return the list
     */
    List<CarritoDetalle> findByCarrito_Id(Long carritoId);

}
