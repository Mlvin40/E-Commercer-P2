package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.AddItemDto;
import ecommerce_api.ecommerce_api.dto.CarritoItemDto;
import ecommerce_api.ecommerce_api.dto.CarritoViewDto;
import ecommerce_api.ecommerce_api.model.Carrito;
import ecommerce_api.ecommerce_api.model.CarritoDetalle;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.CarritoDetalleRepository;
import ecommerce_api.ecommerce_api.repo.CarritoRepository;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * The type Carrito service.
 */
@Service @RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritos;
    private final CarritoDetalleRepository detalles;
    private final ProductoRepository productos;
    private final UsuarioRepository usuarios;

    /**
     * Add item carrito view dto.
     *
     * @param userId     the user id
     * @param productoId the producto id
     * @param cantidad   the cantidad
     * @return the carrito view dto
     */
    @Transactional
    public CarritoViewDto addItem(Long userId, Long productoId, int cantidad) {
        if (cantidad < 1) throw new IllegalArgumentException("Cantidad inválida");

        Usuario u = usuarios.findById(userId).orElseThrow();
        Carrito carrito = carritos.findByUsuario_Id(userId).orElseGet(() -> {
            Carrito c = new Carrito(); c.setUsuario(u); return carritos.save(c);
        });

        Producto p = productos.findById(productoId)
                .filter(x -> "APROBADO".equals(x.getEstadoPublicacion()))
                .orElseThrow(() -> new IllegalArgumentException("Producto no disponible"));

        CarritoDetalle det = detalles.findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .orElseGet(() -> {
                    CarritoDetalle d = new CarritoDetalle();
                    d.setCarrito(carrito); d.setProducto(p); d.setCantidad(0);
                    return d;
                });

        int nueva = det.getCantidad() + cantidad;
        if (nueva > p.getStock()) throw new IllegalArgumentException("Sin stock suficiente");
        det.setCantidad(nueva);
        detalles.save(det);

        return view(userId);
    }

    /**
     * Update qty carrito view dto.
     *
     * @param userId     the user id
     * @param productoId the producto id
     * @param cantidad   the cantidad
     * @return the carrito view dto
     */
    @Transactional
    public CarritoViewDto updateQty(Long userId, Long productoId, int cantidad) {
        if (cantidad < 1) throw new IllegalArgumentException("Cantidad inválida");
        Carrito carrito = carritos.findByUsuario_Id(userId).orElseThrow(() -> new IllegalStateException("Carrito vacío"));
        CarritoDetalle det = detalles.findByCarrito_IdAndProducto_Id(carrito.getId(), productoId).orElseThrow();
        if (cantidad > det.getProducto().getStock()) throw new IllegalArgumentException("Sin stock suficiente");
        det.setCantidad(cantidad);
        detalles.save(det);
        return view(userId);
    }

    /**
     * Remove item carrito view dto.
     *
     * @param userId     the user id
     * @param productoId the producto id
     * @return the carrito view dto
     */
    @Transactional
    public CarritoViewDto removeItem(Long userId, Long productoId) {
        Carrito carrito = carritos.findByUsuario_Id(userId).orElseThrow();
        detalles.findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .ifPresent(detalles::delete);
        return view(userId);
    }

    /**
     * Clear.
     *
     * @param userId the user id
     */
    @Transactional
    public void clear(Long userId) {
        Carrito carrito = carritos.findByUsuario_Id(userId).orElseThrow();
        List<CarritoDetalle> list = detalles.findByCarrito_Id(carrito.getId());
        detalles.deleteAll(list);
    }

    /**
     * View carrito view dto.
     *
     * @param userId the user id
     * @return the carrito view dto
     */
    @Transactional(readOnly = true)
    public CarritoViewDto view(Long userId) {
        Carrito carrito = carritos.findByUsuario_Id(userId).orElse(null);
        if (carrito == null)
            return new CarritoViewDto(null, List.of(), BigDecimal.ZERO);

        List<CarritoItemDto> items = detalles.findByCarrito_Id(carrito.getId()).stream().map(d ->
                new CarritoItemDto(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getProducto().getImagenUrl(),
                        d.getProducto().getPrecio(),
                        d.getCantidad(),
                        d.getProducto().getStock()
                )
        ).toList();

        BigDecimal subtotal = items.stream()
                .map(i -> i.precio().multiply(BigDecimal.valueOf(i.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoViewDto(carrito.getId(), items, subtotal);
    }
}
