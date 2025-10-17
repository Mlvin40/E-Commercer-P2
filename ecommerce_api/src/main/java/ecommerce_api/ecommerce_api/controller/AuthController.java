package ecommerce_api.ecommerce_api.controller;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistroDto dto) {
        service.register(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
