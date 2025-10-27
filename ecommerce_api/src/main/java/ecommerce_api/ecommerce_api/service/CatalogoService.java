// src/main/java/ecommerce_api/ecommerce_api/service/CatalogoService.java
package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.ProductoCardView;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Catalogo service.
 */
@Service
@RequiredArgsConstructor
public class CatalogoService {
    private final ProductoRepository productos;
    private final RatingService ratings;

    /**
     * Listar page.
     *
     * @param meId      the me id
     * @param categoria the categoria
     * @param q         the q
     * @param pageable  the pageable
     * @return the page
     */
    @Transactional(readOnly = true)
    public Page<ProductoCardView> listar(Long meId, String categoria, String q, Pageable pageable) {
        String cat = (categoria == null || categoria.isBlank()) ? null : categoria.trim().toUpperCase();
        String qq  = (q == null || q.isBlank()) ? null : q.trim();

        Page<Producto> page = productos.buscarCatalogoVisible(meId, cat, qq, pageable);

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
