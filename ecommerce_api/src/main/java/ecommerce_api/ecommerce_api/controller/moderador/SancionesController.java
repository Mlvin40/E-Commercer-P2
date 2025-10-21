package ecommerce_api.ecommerce_api.controller.moderador;

import ecommerce_api.ecommerce_api.dto.SancionCreateDto;
import ecommerce_api.ecommerce_api.dto.SancionView;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.SancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moderador/sanciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MODERADOR')")
public class SancionesController {

    private final SancionService sanciones;

    @PostMapping
    public Map<String, Object> crear(@AuthenticationPrincipal AppPrincipal me,
                                     @RequestBody SancionCreateDto dto) {
        Long id = sanciones.crear(me.id(), dto.usuarioId(), dto.motivo());
        return Map.of("id", id, "estado", "ACTIVA");
    }

    @PatchMapping("/{id}/levantar")
    public void levantar(@PathVariable Long id) {
        sanciones.levantar(id);
    }

    @GetMapping
    public List<SancionView> listar(@RequestParam(required = false) Long usuarioId,
                                    @RequestParam(required = false) String estado) {
        return sanciones.listar(usuarioId, estado);
    }
}
