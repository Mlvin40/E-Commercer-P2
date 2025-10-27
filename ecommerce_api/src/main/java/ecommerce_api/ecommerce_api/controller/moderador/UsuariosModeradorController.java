package ecommerce_api.ecommerce_api.controller.moderador;

import ecommerce_api.ecommerce_api.dto.UsuarioLiteView;
import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Usuarios moderador controller.
 */
@RestController
@RequestMapping("/api/moderador/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MODERADOR')")
public class UsuariosModeradorController {

    private final UsuarioRepository usuarios;

    /**
     * Vendedores list.
     *
     * @param q    the q
     * @param page the page
     * @param size the size
     * @return the list
     */
    @GetMapping("/vendedores")
    public List<UsuarioLiteView> vendedores(@RequestParam(required = false) String q,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        var pageRes = (q == null || q.isBlank())
                ? usuarios.findByRolAndActivoTrueOrderByNombreAsc(Role.COMUN, pageable)
                : usuarios.findByRolAndActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(
                Role.COMUN, q.trim(), pageable);

        return pageRes.map(u -> new UsuarioLiteView(u.getId(), u.getNombre(), u.getCorreo()))
                .getContent();
    }
}
