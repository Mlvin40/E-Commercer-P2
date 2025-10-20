package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.ProductoCardView;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comun/catalogo")
@RequiredArgsConstructor
public class CatalogoPrivadoController {
    private final CatalogoService service;

    @GetMapping
    public ResponseEntity<Page<ProductoCardView>> catalogoLogueado(
            @AuthenticationPrincipal AppPrincipal me,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size,
            @RequestParam(required=false) String categoria,
            @RequestParam(required=false, name="q") String q
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPublicacion"));
        return ResponseEntity.ok(service.listar(me.id(), categoria, q, pageable));
    }
}