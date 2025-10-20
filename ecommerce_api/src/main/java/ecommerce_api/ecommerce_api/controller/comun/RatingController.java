package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.RatingCreateDto;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/comun/ratings") @RequiredArgsConstructor
public class RatingController {

    private final RatingService service;

    @PostMapping
    public ResponseEntity<?> crear(@AuthenticationPrincipal AppPrincipal me,
                                   @RequestBody RatingCreateDto dto) {
        service.calificar(me.id(), dto);
        return ResponseEntity.status(201).build();
    }
}
