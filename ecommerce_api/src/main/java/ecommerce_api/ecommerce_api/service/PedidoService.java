package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.PedidoListItemDto;
import ecommerce_api.ecommerce_api.model.Pedido;
import ecommerce_api.ecommerce_api.repo.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidos;

    @Transactional(readOnly = true)
    public List<PedidoListItemDto> listar(Long userId) {
        return pedidos.findByUsuario_IdOrderByFechaPedidoDesc(userId).stream()
                .map(p -> new PedidoListItemDto(
                        p.getId(),
                        p.getFechaPedido(),
                        p.getFechaEstimadaEntrega(),
                        p.getEstado(),
                        p.getTotal()
                )).toList();
    }
}
