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

@Service
@RequiredArgsConstructor
public class SancionService {

    private final SancionRepository repo;
    private final UsuarioRepository usuarios;

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

    @Transactional
    public void levantar(Long sancionId) {
        Sancion s = repo.findById(sancionId).orElseThrow();
        s.setEstado("LEVANTADA");
        repo.save(s);
    }

    @Transactional(readOnly = true)
    public boolean tieneActiva(Long usuarioId) {
        return repo.existeActivaPara(usuarioId);
    }

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
