package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.*;
import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import ecommerce_api.ecommerce_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * The type Auth service.
 */
@Service @RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    /**
     * Register.
     *
     * @param dto the dto
     */
    public void register(RegistroDto dto) {
        var correo = dto.correo().toLowerCase().trim();
        if (repo.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya está registrado.");
            //return;
        }
        Usuario u = Usuario.builder()
                .nombre(dto.nombre().trim())
                .correo(correo)
                .password(encoder.encode(dto.password()))
                .rol(Role.COMUN)   // forza rol COMUN
                .activo(true)
                .build();
        repo.save(u);
    }

    /**
     * Login auth response.
     *
     * @param dto the dto
     * @return the auth response
     */
    public AuthResponse login(LoginDto dto) {
        var correo = dto.correo().toLowerCase().trim();

        Usuario u = repo.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!u.getActivo()) throw new IllegalStateException("Usuario inactivo.");
        if (!encoder.matches(dto.password(), u.getPassword()))
            throw new IllegalArgumentException("Credenciales inválidas.");

        Map<String, Object> claims = Map.of("role", u.getRol().name(), "uid", u.getId());
        String access = jwt.generateToken(u.getCorreo(), claims);
        String refresh = jwt.generateRefreshToken(u.getCorreo());

        return new AuthResponse(access, refresh, "Bearer", 3600000);
    }
}