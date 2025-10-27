package ecommerce_api.ecommerce_api.controller.logistica;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.service.LogisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Logistica pedidos controller.
 */
@RestController
@RequestMapping("/api/logistica/pedidos")
@RequiredArgsConstructor
public class LogisticaPedidosController {
    private final LogisticaService service;

    /**
     * Listar response entity.
     *
     * @param page the page
     * @param size the size
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<Page<PedidoLogisticaRow>> listar(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size
    ) {
        var sort = Sort.by(Sort.Direction.ASC, "fechaEstimadaEntrega").and(Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(service.listarEnCurso(PageRequest.of(page, size, sort)));
    }

    /**
     * Reprogramar response entity.
     *
     * @param id   the id
     * @param body the body
     * @return the response entity
     */
    @PatchMapping("/{id}/fecha-entrega")
    public ResponseEntity<PedidoLogisticaRow> reprogramar(
            @PathVariable Long id,
            @RequestBody ReprogramarFechaRequest body
    ) {
        return ResponseEntity.ok(service.reprogramarFecha(id, body.fechaEstimadaEntrega()));
    }

    /**
     * Entregar response entity.
     *
     * @param id   the id
     * @param body the body
     * @return the response entity
     */
    @PostMapping("/{id}/entregar")
    public ResponseEntity<Void> entregar(
            @PathVariable Long id,
            @RequestBody(required = false) EntregarRequest body
    ) {
        service.marcarEntregado(id, body != null ? body.fechaEntregada() : null);
        return ResponseEntity.noContent().build();
    }
}
