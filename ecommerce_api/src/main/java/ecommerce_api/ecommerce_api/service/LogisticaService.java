package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.PedidoLogisticaRow;
import ecommerce_api.ecommerce_api.model.Pedido;
import ecommerce_api.ecommerce_api.model.PedidoDetalle;
import ecommerce_api.ecommerce_api.repo.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LogisticaService {
    private final PedidoRepository pedidos;

    public Page<PedidoLogisticaRow> listarEnCurso(Pageable pageable) {
        return pedidos.findByEstado("EN_CURSO", pageable)
                .map(this::toRow);
    }

    @Transactional
    public PedidoLogisticaRow reprogramarFecha(Long id, LocalDate nuevaFecha) {
        if (nuevaFecha == null) throw new IllegalArgumentException("Fecha requerida");
        if (nuevaFecha.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("No puede ser en el pasado");

        Pedido p = pedidos.findByIdAndEstado(id, "EN_CURSO")
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado o no está EN_CURSO"));

        p.setFechaEstimadaEntrega(nuevaFecha);
        return toRow(p);
    }

    @Transactional
    public void marcarEntregado(Long id, LocalDate fechaEntregada) {
        Pedido p = pedidos.findByIdAndEstado(id, "EN_CURSO")
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado o no está EN_CURSO"));

        p.setEstado("ENTREGADO");
        p.setFechaEntregada(fechaEntregada != null ? fechaEntregada : LocalDate.now());
    }

    private PedidoLogisticaRow toRow(Pedido p) {
        int items = p.getDetalles() == null ? 0 :
                p.getDetalles().stream().mapToInt(PedidoDetalle::getCantidad).sum();

        return new PedidoLogisticaRow(
                p.getId(),
                p.getUsuario().getNombre(),
                p.getTotal(),
                p.getFechaPedido(),
                p.getFechaEstimadaEntrega(),
                p.getEstado(),
                items
        );
    }
}
