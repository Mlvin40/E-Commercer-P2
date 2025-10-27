package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.AddItemDto;
import ecommerce_api.ecommerce_api.dto.CarritoViewDto;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * The type Carrito controller.
 */
@RestController @RequestMapping("/api/comun/carrito") @RequiredArgsConstructor
public class CarritoController {

    private final CarritoService service;

    /**
     * Ver response entity.
     *
     * @param me the me
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<CarritoViewDto> ver(@AuthenticationPrincipal AppPrincipal me) {
        return ResponseEntity.ok(service.view(me.id()));
    }

    /**
     * Add response entity.
     *
     * @param me  the me
     * @param dto the dto
     * @return the response entity
     */
    @PostMapping("/items")
    public ResponseEntity<CarritoViewDto> add(@AuthenticationPrincipal AppPrincipal me,
                                              @RequestBody AddItemDto dto) {
        return ResponseEntity.ok(service.addItem(me.id(), dto.productoId(), dto.cantidad()));
    }

    /**
     * Update response entity.
     *
     * @param me         the me
     * @param productoId the producto id
     * @param dto        the dto
     * @return the response entity
     */
    @PutMapping("/items/{productoId}")
    public ResponseEntity<CarritoViewDto> update(@AuthenticationPrincipal AppPrincipal me,
                                                 @PathVariable Long productoId,
                                                 @RequestBody ecommerce_api.ecommerce_api.dto.UpdateQtyDto dto) {
        return ResponseEntity.ok(service.updateQty(me.id(), productoId, dto.cantidad()));
    }

    /**
     * Remove response entity.
     *
     * @param me         the me
     * @param productoId the producto id
     * @return the response entity
     */
    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoViewDto> remove(@AuthenticationPrincipal AppPrincipal me,
                                                 @PathVariable Long productoId) {
        return ResponseEntity.ok(service.removeItem(me.id(), productoId));
    }

    /**
     * Clear response entity.
     *
     * @param me the me
     * @return the response entity
     */
    @DeleteMapping
    public ResponseEntity<?> clear(@AuthenticationPrincipal AppPrincipal me) {
        service.clear(me.id());
        return ResponseEntity.noContent().build();
    }
}
