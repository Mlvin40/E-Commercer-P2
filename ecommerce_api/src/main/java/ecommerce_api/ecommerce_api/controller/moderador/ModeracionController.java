package ecommerce_api.ecommerce_api.controller.moderador;

import ecommerce_api.ecommerce_api.dto.ModeracionDecisionDto;
import ecommerce_api.ecommerce_api.dto.ProductoPendienteView;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.ModeracionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Moderacion controller.
 */
@RestController
@RequestMapping("/api/moderador/revisiones")
@RequiredArgsConstructor
public class ModeracionController {

    private final ModeracionService service;

    /**
     * Pendientes response entity.
     *
     * @return the response entity
     */
    @PreAuthorize("hasRole('MODERADOR')")
    @GetMapping("/pendientes")
    public ResponseEntity<List<ProductoPendienteView>> pendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    /**
     * Aprobar response entity.
     *
     * @param me         the me
     * @param productoId the producto id
     * @return the response entity
     */
    @PreAuthorize("hasRole('MODERADOR')")
    @PostMapping("/{productoId}/aprobar")
    public ResponseEntity<?> aprobar(@AuthenticationPrincipal AppPrincipal me,
                                     @PathVariable Long productoId) {
        service.aprobar(me.id(), productoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechazar response entity.
     *
     * @param me         the me
     * @param productoId the producto id
     * @param body       the body
     * @return the response entity
     */
    @PreAuthorize("hasRole('MODERADOR')")
    @PostMapping("/{productoId}/rechazar")
    public ResponseEntity<?> rechazar(@AuthenticationPrincipal AppPrincipal me,
                                      @PathVariable Long productoId,
                                      @RequestBody(required = false) ModeracionDecisionDto body) {
        service.rechazar(me.id(), productoId, body != null ? body.comentario() : null);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retirar publicado response entity.
     *
     * @param me         the me
     * @param productoId the producto id
     * @param body       the body
     * @return the response entity
     */
    // Cuando un producto ya fue aprobado y publicado, el moderador puede retirarlo
    @PreAuthorize("hasRole('MODERADOR')")
    @PostMapping("/productos/{productoId}/retirar")
    public ResponseEntity<?> retirarPublicado(@AuthenticationPrincipal AppPrincipal me,
                                              @PathVariable Long productoId,
                                              @RequestBody(required = false) ModeracionDecisionDto body) {
        service.retirarPublicado(me.id(), productoId, body != null ? body.comentario() : null);
        return ResponseEntity.noContent().build();
    }
}
