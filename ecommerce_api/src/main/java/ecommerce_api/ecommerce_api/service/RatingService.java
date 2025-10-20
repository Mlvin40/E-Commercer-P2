package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.RatingCreateDto;
import ecommerce_api.ecommerce_api.dto.RatingSummaryDto;
import ecommerce_api.ecommerce_api.model.Calificacion;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.CalificacionRepository;
import ecommerce_api.ecommerce_api.repo.PedidoDetalleRepository;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class RatingService {

    private final PedidoDetalleRepository pedidoDetalles;
    private final CalificacionRepository calificaciones;
    private final ProductoRepository productos;

    @Transactional
    public void calificar(Long userId, RatingCreateDto dto) {
        if (dto.estrellas() == null || dto.estrellas() < 1 || dto.estrellas() > 5)
            throw new IllegalArgumentException("Estrellas 1..5");

        if (!pedidoDetalles.userBoughtProduct(userId, dto.productoId()))
            throw new IllegalArgumentException("Debes haber comprado este producto");

        if (calificaciones.existsByUsuario_IdAndProducto_Id(userId, dto.productoId()))
            throw new IllegalArgumentException("Ya calificaste este producto");

        Producto p = productos.findById(dto.productoId()).orElseThrow();

        Calificacion c = new Calificacion();
        Usuario u = new Usuario(); u.setId(userId);
        c.setUsuario(u);
        c.setProducto(p);
        c.setEstrellas(dto.estrellas());
        c.setComentario(dto.comentario());
        calificaciones.save(c);
    }

    @Transactional(readOnly = true)
    public RatingSummaryDto resumen(Long productoId) {
        return new RatingSummaryDto(
                calificaciones.avgForProduct(productoId),
                calificaciones.countForProduct(productoId)
        );
    }
}
