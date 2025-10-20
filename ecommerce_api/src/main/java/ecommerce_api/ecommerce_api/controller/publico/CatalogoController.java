package ecommerce_api.ecommerce_api.controller.publico;

import ecommerce_api.ecommerce_api.dto.ProductoCardView;
import ecommerce_api.ecommerce_api.dto.ProductoDetalleView;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import ecommerce_api.ecommerce_api.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService service;
    private final ProductoRepository productos;

    @GetMapping("/catalogo")
    public ResponseEntity<Page<ProductoCardView>> catalogo(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size,
            @RequestParam(required=false) String categoria,
            @RequestParam(required=false, name="q") String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));
        // Público → no hay usuario logueado, así que excluidoId = null
        return ResponseEntity.ok(service.listar(null, categoria, q, pageable));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDetalleView> detalle(@PathVariable Long id) {
        var p = productos.findById(id)
                .filter(pp -> "APROBADO".equals(pp.getEstadoPublicacion()))
                .orElseThrow(() -> new IllegalArgumentException("Producto no disponible"));

        var dto = new ProductoDetalleView(
                p.getId(), p.getNombre(), p.getDescripcion(), p.getImagenUrl(),
                p.getPrecio(), p.getStock(), p.getEstado(), p.getCategoria(),
                p.getVendedor().getNombre()
        );
        return ResponseEntity.ok(dto);
    }

}
