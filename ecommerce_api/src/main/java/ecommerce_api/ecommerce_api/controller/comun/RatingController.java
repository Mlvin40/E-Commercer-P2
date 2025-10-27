package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.RatingCreateDto;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * The type Rating controller.
 */
@RestController @RequestMapping("/api/comun/ratings") @RequiredArgsConstructor
public class RatingController {

    private final RatingService service;

    /**
     * Crear response entity.
     *
     * @param me  the me
     * @param dto the dto
     * @return the response entity
     */
    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<?> crear(@AuthenticationPrincipal AppPrincipal me,
                                   @RequestBody RatingCreateDto dto) {
        service.calificar(me.id(), dto);
        return ResponseEntity.status(201).build();
    }
}
