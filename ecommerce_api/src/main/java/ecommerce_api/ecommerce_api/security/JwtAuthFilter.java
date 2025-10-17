package ecommerce_api.ecommerce_api.security;

import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UsuarioRepository repo;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwt.parse(token).getBody();
                String correo = claims.getSubject();
                String role = claims.get("role", String.class);

                repo.findByCorreo(correo).ifPresent(u -> {
                    var auth = new UsernamePasswordAuthenticationToken(
                            correo, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
            } catch (Exception ignored) { /* token inválido/expirado */ }
        }
        chain.doFilter(req, res);
    }
}