package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Admin usuario service.
 */
@Service
@RequiredArgsConstructor
public class AdminUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

    /**
     * Listar page.
     *
     * @param q        the q
     * @param pageable the pageable
     * @return the page
     */

    // q es el filtro de búsqueda nombre o correo
    public Page<EmpleadoView> listar(String q, Pageable pageable) {
        var roles = java.util.List.of(
                Role.ADMIN.name(), Role.MODERADOR.name(), Role.LOGISTICA.name()
        );
        String filtro = (q == null || q.isBlank()) ? null : q.trim();

        Page<Usuario> page = usuarioRepository.buscarEmpleados(roles, filtro, pageable);
        return page.map(this::toView);
    }

    private EmpleadoView toView(Usuario u) {
        return new EmpleadoView(
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                u.getRol(),
                u.getActivo(),
                u.getFechaCreacion()
        );
    }

    /**
     * Crear empleado view.
     *
     * @param req the req
     * @return the empleado view
     */
    public EmpleadoView crear(CrearEmpleadoRequest req) {
        var correo = req.correo().toLowerCase().trim();
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        var rol = req.rol() == null ? Role.MODERADOR : req.rol();
        validarRolEmpleado(rol);

        var u = Usuario.builder()
                .nombre(req.nombre().trim())
                .correo(correo)
                .password(encoder.encode(req.password()))
                .rol(rol)
                .activo(true)
                .build();

        usuarioRepository.save(u);
        return toView(u);
    }

    /**
     * Actualizar empleado view.
     *
     * @param id  the id
     * @param req the req
     * @return the empleado view
     */
    public EmpleadoView actualizar(Long id, ActualizarEmpleadoRequest req) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado."));

        if (req.nombre() != null) u.setNombre(req.nombre().trim());

        if (req.correo() != null) {
            var correo = req.correo().toLowerCase().trim();
            if (!correo.equals(u.getCorreo()) && usuarioRepository.existsByCorreo(correo))
                throw new IllegalArgumentException("El correo ya está registrado.");
            u.setCorreo(correo);
        }

        if (req.activo() != null) u.setActivo(req.activo());

        if (req.password() != null && !req.password().isBlank())
            u.setPassword(encoder.encode(req.password()));

        usuarioRepository.save(u);
        return toView(u);
    }

    private void validarRolEmpleado(Role r) {
        if (r != Role.ADMIN && r != Role.MODERADOR && r != Role.LOGISTICA)
            throw new IllegalArgumentException("Rol no permitido para empleado.");
    }


}
