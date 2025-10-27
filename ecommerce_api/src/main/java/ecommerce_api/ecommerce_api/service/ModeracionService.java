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

/**
 * The type Moderacion service.
 */
@Service
@RequiredArgsConstructor
public class ModeracionService {

    private final ProductoRepository productos;
    private final ProductoRevisionRepository revisiones;
    private final UsuarioRepository usuarios;

    /**
     * Listar pendientes list.
     *
     * @return the list
     */
    @Transactional(readOnly = true)
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

    /**
     * Aprobar.
     *
     * @param moderadorId the moderador id
     * @param productoId  the producto id
     */
    @Transactional
    public void aprobar(Long moderadorId, Long productoId) {
        Usuario mod = usuarios.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));

        if (!("MODERADOR".equals(mod.getRol().name()) || "ADMIN".equals(mod.getRol().name()))) {
            throw new IllegalStateException("No autorizado");
        }

        Producto p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

        ProductoRevision rev = revisiones
                .findFirstByProducto_IdAndEstadoOrderByCreadoEnAsc(productoId, "PENDIENTE")
                .orElseThrow(() -> new IllegalArgumentException("No hay revisión pendiente para este producto."));

        rev.setEstado("APROBADO");
        rev.setModerador(mod);
        rev.setResueltoEn(Instant.now());
        revisiones.save(rev);

        p.setEstadoPublicacion("APROBADO");
        productos.save(p);
    }

    /**
     * Rechazar.
     *
     * @param moderadorId the moderador id
     * @param productoId  the producto id
     * @param comentario  the comentario
     */
    @Transactional
    public void rechazar(Long moderadorId, Long productoId, String comentario) {
        Usuario mod = usuarios.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));

        if (!("MODERADOR".equals(mod.getRol().name()) || "ADMIN".equals(mod.getRol().name()))) {
            throw new IllegalStateException("No autorizado");
        }

        Producto p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

        ProductoRevision rev = revisiones
                .findFirstByProducto_IdAndEstadoOrderByCreadoEnAsc(productoId, "PENDIENTE")
                .orElseThrow(() -> new IllegalArgumentException("No hay revisión pendiente para este producto."));

        rev.setEstado("RECHAZADO");
        rev.setModerador(mod);
        rev.setComentario(comentario);
        rev.setResueltoEn(Instant.now());
        revisiones.save(rev);

        p.setEstadoPublicacion("RECHAZADO");
        productos.save(p);
    }

    /**
     * Retira un producto ya publicado (estado_publicacion = APROBADO) y registra
     * el evento en el historial (producto_revision) como RECHAZADO con comentario.
     *
     * @param moderadorId the moderador id
     * @param productoId  the producto id
     * @param comentario  the comentario
     */
    @Transactional
    public void retirarPublicado(Long moderadorId, Long productoId, String comentario) {
        Usuario mod = usuarios.findById(moderadorId)
                .orElseThrow(() -> new IllegalArgumentException("Moderador no encontrado"));

        if (!("MODERADOR".equals(mod.getRol().name()) || "ADMIN".equals(mod.getRol().name()))) {
            throw new IllegalStateException("No autorizado");
        }

        Producto p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

        if (!"APROBADO".equals(p.getEstadoPublicacion())) {
            throw new IllegalStateException("Solo se pueden retirar productos publicados (APROBADO).");
        }

        // Registrar historial de retiro
        ProductoRevision rev = new ProductoRevision();
        rev.setProducto(p);
        rev.setSolicitadoPor(p.getVendedor()); // quien creó el producto
        rev.setEstado("RECHAZADO");
        rev.setModerador(mod);
        rev.setComentario((comentario == null || comentario.isBlank())
                ? "Retirado por moderación"
                : comentario);
        rev.setCreadoEn(Instant.now());
        rev.setResueltoEn(Instant.now());
        revisiones.save(rev);

        // Cambiar visibilidad
        p.setEstadoPublicacion("RECHAZADO");
        productos.save(p);
    }

}
