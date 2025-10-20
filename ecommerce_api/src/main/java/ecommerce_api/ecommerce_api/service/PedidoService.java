package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.PedidoItemParaRatingDto;
import ecommerce_api.ecommerce_api.dto.PedidoListItemDto;
import ecommerce_api.ecommerce_api.model.Pedido;
import ecommerce_api.ecommerce_api.repo.CalificacionRepository;
import ecommerce_api.ecommerce_api.repo.PedidoDetalleRepository;
import ecommerce_api.ecommerce_api.repo.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidos;
    private final PedidoDetalleRepository pedidoDetalles;
    private final CalificacionRepository calificaciones;

    @Transactional(readOnly = true)
    public List<PedidoListItemDto> listar(Long userId) {
        return pedidos.findByUsuario_IdOrderByFechaPedidoDesc(userId).stream()
                .map(p -> new PedidoListItemDto(
                        p.getId(), p.getFechaPedido(), p.getFechaEstimadaEntrega(),
                        p.getEstado(), p.getTotal()
                )).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoItemParaRatingDto> detalleParaRating(Long userId, Long pedidoId) {
        Pedido pedido = pedidos.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no existe"));
        if (!pedido.getUsuario().getId().equals(userId)) {
            throw new IllegalArgumentException("No autorizado");
        }

        return pedidoDetalles.findByPedido_Id(pedidoId).stream()
                .map(d -> new PedidoItemParaRatingDto(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getProducto().getImagenUrl(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        calificaciones.existsByUsuario_IdAndProducto_Id(userId, d.getProducto().getId())
                ))
                .toList();
    }
}
