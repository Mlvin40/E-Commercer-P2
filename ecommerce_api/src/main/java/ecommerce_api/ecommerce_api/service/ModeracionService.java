package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.ProductoPendienteView;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.ProductoRevision;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import ecommerce_api.ecommerce_api.repo.ProductoRevisionRepository;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeracionService {

    private final ProductoRepository productos;
    private final ProductoRevisionRepository revisiones;
    private final UsuarioRepository usuarios;

    public List<ProductoPendienteView> listarPendientes() {
        return revisiones.findByEstadoOrderByCreadoEnAsc("PENDIENTE")
                .stream()
                .map(r -> new ProductoPendienteView(
                        r.getProducto().getId(),
                        r.getProducto().getNombre(),
                        r.getProducto().getCategoria(),
                        r.getProducto().getEstado(),
                        r.getProducto().getImagenUrl(),
                        r.getSolicitadoPor().getNombre(),
                        r.getSolicitadoPor().getCorreo(),
                        r.getCreadoEn()
                ))
                .toList();
    }

    @Transactional
    public void aprobar(Long moderadorId, Long productoId) {
        Usuario mod = usuarios.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));
        if (!"MODERADOR".equals(mod.getRol().name())) {
            throw new IllegalStateException("No autorizado");
        }

        Producto p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

        // cierra revisión pendiente (si existe)
        revisiones.findByEstadoOrderByCreadoEnAsc("PENDIENTE").stream()
                .filter(r -> r.getProducto().getId().equals(productoId))
                .findFirst()
                .ifPresent(r -> {
                    r.setEstado("APROBADO");
                    r.setModerador(mod);
                    r.setResueltoEn(Instant.now());
                    revisiones.save(r);
                });

        p.setEstadoPublicacion("APROBADO");
        productos.save(p);
    }

    @Transactional
    public void rechazar(Long moderadorId, Long productoId, String comentario) {
        Usuario mod = usuarios.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));
        if (!"MODERADOR".equals(mod.getRol().name())) {
            throw new IllegalStateException("No autorizado");
        }

        Producto p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

        revisiones.findByEstadoOrderByCreadoEnAsc("PENDIENTE").stream()
                .filter(r -> r.getProducto().getId().equals(productoId))
                .findFirst()
                .ifPresent(r -> {
                    r.setEstado("RECHAZADO");
                    r.setModerador(mod);
                    r.setComentario(comentario);
                    r.setResueltoEn(Instant.now());
                    revisiones.save(r);
                });

        p.setEstadoPublicacion("RECHAZADO");
        productos.save(p);
    }
}
