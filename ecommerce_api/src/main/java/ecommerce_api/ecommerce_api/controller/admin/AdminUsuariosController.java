package ecommerce_api.ecommerce_api.controller.admin;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.service.AdminUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Admin usuarios controller.
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuariosController {
    private final AdminUsuarioService service;

    /**
     * Listar response entity.
     *
     * @param q    the q
     * @param page the page
     * @param size the size
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<Page<EmpleadoView>> listar(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size
    ) {
        var sort = Sort.by(Sort.Direction.ASC, "nombre").and(Sort.by(Sort.Direction.ASC, "correo"));
        return ResponseEntity.ok(service.listar(q, PageRequest.of(page, size, sort)));
    }

    /**
     * Crear response entity.
     *
     * @param req the req
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<EmpleadoView> crear(@Validated @RequestBody CrearEmpleadoRequest req) {
        var view = service.crear(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(view);
    }

    /**
     * Actualizar response entity.
     *
     * @param id  the id
     * @param req the req
     * @return the response entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EmpleadoView> actualizar(
            @PathVariable Long id,
            @Validated @RequestBody ActualizarEmpleadoRequest req
    ) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }
}
