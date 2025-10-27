package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.ProductoUpsertDto;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * The type Mis productos controller.
 */
@RestController
@RequestMapping("/api/comun/mis-productos")
@RequiredArgsConstructor
public class MisProductosController {

    private final ProductoService service;

    /**
     * Listar response entity.
     *
     * @param me the me
     * @return the response entity
     */
    @PreAuthorize("hasRole('COMUN')")
    @GetMapping
    public ResponseEntity<?> listar(@AuthenticationPrincipal AppPrincipal me) {
        return ResponseEntity.ok(service.listarMisProductos(me.id()));
    }

    /**
     * Actualizar response entity.
     *
     * @param me  the me
     * @param id  the id
     * @param dto the dto
     * @return the response entity
     */
    @PreAuthorize("hasRole('COMUN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@AuthenticationPrincipal AppPrincipal me,
                                        @PathVariable Long id,
                                        @RequestBody ProductoUpsertDto dto) {
        service.actualizar(me.id(), id, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar response entity.
     *
     * @param me the me
     * @param id the id
     * @return the response entity
     */
    @PreAuthorize("hasRole('COMUN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@AuthenticationPrincipal AppPrincipal me,
                                      @PathVariable Long id) {
        service.eliminar(me.id(), id);
        return ResponseEntity.noContent().build();
    }

}
