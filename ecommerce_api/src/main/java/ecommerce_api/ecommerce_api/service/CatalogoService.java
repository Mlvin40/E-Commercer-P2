package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.ProductoCardView;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// CatalogoService.java
@Service
@RequiredArgsConstructor
public class CatalogoService {
    private final ProductoRepository productos;
    private final RatingService ratings;

    @Transactional(readOnly = true)
    public Page<ProductoCardView> listar(Long meId, String categoria, String q, Pageable pageable) {
        final String APROBADO = "APROBADO";
        Page<Producto> page;

        boolean logged = (meId != null);

        if (logged) {
            if (q != null && !q.isBlank()) {
                page = productos.findByEstadoPublicacionAndNombreContainingIgnoreCaseAndVendedor_IdNot(
                        APROBADO, q.trim(), meId, pageable);
            } else if (categoria != null && !categoria.isBlank()) {
                page = productos.findByEstadoPublicacionAndCategoriaAndVendedor_IdNot(
                        APROBADO, categoria.trim().toUpperCase(), meId, pageable);
            } else {
                page = productos.findByEstadoPublicacionAndVendedor_IdNot(APROBADO, meId, pageable);
            }
        } else {
            if (q != null && !q.isBlank()) {
                page = productos.findByEstadoPublicacionAndNombreContainingIgnoreCase(APROBADO, q.trim(), pageable);
            } else if (categoria != null && !categoria.isBlank()) {
                page = productos.findByEstadoPublicacionAndCategoria(APROBADO, categoria.trim().toUpperCase(), pageable);
            } else {
                page = productos.findByEstadoPublicacion(APROBADO, pageable);
            }
        }

        return page.map(p -> {
            var s = ratings.resumen(p.getId());
            return new ProductoCardView(
                    p.getId(), p.getNombre(), p.getImagenUrl(), p.getPrecio(), p.getStock(), p.getCategoria(),
                    p.getVendedor().getNombre(), p.getVendedor().getCorreo(),
                    s.avg(), s.count()
            );
        });
    }
}

