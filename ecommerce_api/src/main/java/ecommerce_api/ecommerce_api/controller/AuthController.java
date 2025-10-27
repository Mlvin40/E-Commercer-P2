package ecommerce_api.ecommerce_api.controller;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Auth controller.
 */
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    /**
     * Register response entity.
     *
     * @param dto the dto
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistroDto dto) {
        service.register(dto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Login response entity.
     *
     * @param dto the dto
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
