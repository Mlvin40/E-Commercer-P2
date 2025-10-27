package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.SancionView;
import ecommerce_api.ecommerce_api.model.Sancion;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.SancionRepository;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * The type Sancion service.
 */
@Service
@RequiredArgsConstructor
public class SancionService {

    private final SancionRepository repo;
    private final UsuarioRepository usuarios;

    /**
     * Crear long.
     *
     * @param moderadorId the moderador id
     * @param usuarioId   the usuario id
     * @param motivo      the motivo
     * @return the long
     */
    @Transactional
    public Long crear(Long moderadorId, Long usuarioId, String motivo) {
        Usuario mod = usuarios.findById(moderadorId).orElseThrow();
        Usuario usr = usuarios.findById(usuarioId).orElseThrow();

        Sancion s = new Sancion();
        s.setModerador(mod);
        s.setUsuario(usr);
        s.setMotivo(Objects.requireNonNullElse(motivo, "Sin detalle"));
        s.setEstado("ACTIVA");
        repo.save(s);
        return s.getId();
    }

    /**
     * Levantar.
     *
     * @param sancionId the sancion id
     */
    @Transactional
    public void levantar(Long sancionId) {
        Sancion s = repo.findById(sancionId).orElseThrow();
        s.setEstado("LEVANTADA");
        repo.save(s);
    }

    /**
     * Tiene activa boolean.
     *
     * @param usuarioId the usuario id
     * @return the boolean
     */
    @Transactional(readOnly = true)
    public boolean tieneActiva(Long usuarioId) {
        return repo.existeActivaPara(usuarioId);
    }

    /**
     * Listar list.
     *
     * @param usuarioId the usuario id
     * @param estado    the estado
     * @return the list
     */
    @Transactional(readOnly = true)
    public List<SancionView> listar(Long usuarioId, String estado) {
        return repo.buscar(usuarioId, estado).stream().map(s -> new SancionView(
                s.getId(),
                s.getUsuario().getId(),
                s.getUsuario().getNombre(),
                s.getModerador().getId(),
                s.getModerador().getNombre(),
                s.getMotivo(),
                s.getFecha(),
                s.getEstado()
        )).toList();
    }
}
