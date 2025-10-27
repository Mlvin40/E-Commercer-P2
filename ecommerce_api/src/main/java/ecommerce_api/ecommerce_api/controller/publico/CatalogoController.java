package ecommerce_api.ecommerce_api.controller.publico;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import ecommerce_api.ecommerce_api.service.CatalogoService;
import ecommerce_api.ecommerce_api.service.RatingService;
import ecommerce_api.ecommerce_api.service.SancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Catalogo controller.
 */
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService service;
    private final ProductoRepository productos;
    private final RatingService ratingService;
    private final SancionService sancionService;

    /**
     * Catalogo response entity.
     *
     * @param page      the page
     * @param size      the size
     * @param categoria the categoria
     * @param q         the q
     * @return the response entity
     */
    @GetMapping("/catalogo")
    public ResponseEntity<Page<ProductoCardView>> catalogo(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size,
            @RequestParam(required=false) String categoria,
            @RequestParam(required=false, name="q") String q
    ) {
        Pageable pageable = PageRequest.of(page, size /*Sort.by(Sort.Direction.DESC, "fechaPublicacion")*/);
        return ResponseEntity.ok(service.listar(null, categoria, q, pageable)); // público
    }

    /**
     * Detalle response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDetalleView> detalle(@PathVariable Long id) {
        Producto p = productos.findById(id)
                .filter(pp -> "APROBADO".equals(pp.getEstadoPublicacion()))
                .orElseThrow(() -> new IllegalArgumentException("Producto no disponible"));

        if (sancionService.tieneActiva(p.getVendedor().getId())) {
            throw new IllegalArgumentException("Producto no disponible");
        }

        var sum = ratingService.resumen(id);
        var dto = new ProductoDetalleView(
                p.getId(), p.getNombre(), p.getDescripcion(), p.getImagenUrl(),
                p.getPrecio(), p.getStock(), p.getEstado(), p.getCategoria(),
                p.getVendedor().getNombre(),
                sum.avg(), sum.count()
        );
        return ResponseEntity.ok(dto);
    }

    /**
     * Comentarios response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/productos/{id}/comentarios")
    public ResponseEntity<List<CalificacionView>> comentarios(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.listarComentarios(id));
    }
}
