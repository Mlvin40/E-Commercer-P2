package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.ProductoUpsertDto;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

/**
 * The type Producto controller.
 */
@RestController
@RequestMapping("/api/comun/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    /**
     * Crear response entity.
     *
     * @param me  the me
     * @param dto the dto
     * @return the response entity
     */
    @PreAuthorize("hasRole('COMUN')")
    @PostMapping
    public ResponseEntity<?> crear(@AuthenticationPrincipal AppPrincipal me,
                                   @Valid @RequestBody ProductoUpsertDto dto) {

        var creado = service.crear(me.id(), dto); // pasamos el id del dueño
        return ResponseEntity
                .created(URI.create("/api/comun/productos/" + creado.getId()))
                .body(Map.of("id", creado.getId()));
    }

//    @PreAuthorize("hasRole('COMUN')")
//    @PutMapping("/{id}")
//    public ResponseEntity<?> actualizar(@AuthenticationPrincipal AppPrincipal me,
//                                        @PathVariable Long id,
//                                        @Valid @RequestBody ProductoUpsertDto dto) {
//        service.actualizar(me.id(), id, dto); // validar que el producto sea del usuario
//        return ResponseEntity.noContent().build();
//    }
}
