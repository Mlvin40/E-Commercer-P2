// src/main/java/ecommerce_api/ecommerce_api/controller/comun/CheckoutController.java
package ecommerce_api.ecommerce_api.controller.comun;

import ecommerce_api.ecommerce_api.dto.CheckoutRequest;
import ecommerce_api.ecommerce_api.dto.CheckoutResponse;
import ecommerce_api.ecommerce_api.dto.TarjetaView;
import ecommerce_api.ecommerce_api.security.AppPrincipal;
import ecommerce_api.ecommerce_api.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/comun/checkout") @RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService service;

    @GetMapping("/tarjetas")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<TarjetaView>> tarjetas(@AuthenticationPrincipal AppPrincipal me) {
        System.out.println("GET /tarjetas de user=" + me.id());
        return ResponseEntity.ok(service.listarTarjetas(me.id()));
    }

    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<CheckoutResponse> pagar(@AuthenticationPrincipal AppPrincipal me,
                                                  @RequestBody CheckoutRequest req) {
        return ResponseEntity.ok(service.pagar(me.id(), req));
    }
}
