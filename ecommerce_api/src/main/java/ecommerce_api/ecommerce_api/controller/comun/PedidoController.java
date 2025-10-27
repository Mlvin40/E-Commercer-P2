package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * The type Pedido controller.
 */
@RestController @RequestMapping("/api/comun/pedidos") @RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    /**
     * Listar response entity.
     *
     * @param me the me
     * @return the response entity
     */
    @GetMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<?> listar(@AuthenticationPrincipal AppPrincipal me) {
        return ResponseEntity.ok(service.listar(me.id()));
    }

    /**
     * Detalle response entity.
     *
     * @param me the me
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<?> detalle(@AuthenticationPrincipal AppPrincipal me,
                                     @PathVariable Long id) {
        return ResponseEntity.ok(service.detalleParaRating(me.id(), id));
    }
}
