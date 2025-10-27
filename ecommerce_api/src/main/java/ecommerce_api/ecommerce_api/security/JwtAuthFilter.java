package ecommerce_api.ecommerce_api.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * The type Jwt auth filter.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    // Este metodo lo que hace es interceptar cada solicitud HTTP entrante
    // y buscar un token JWT en el encabezado Authorization.
    // Si encuentra un token válido, extrae la información del usuario
    // y establece el contexto de seguridad para que la aplicación
    // reconozca al usuario autenticado.
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwt.parse(token).getBody();

                String correo = claims.getSubject();               // sub = correo
                String role   = claims.get("role", String.class);  // "Rol".
                Number uidNum = claims.get("uid", Number.class);   // Viene del AuthService
                Long uid = uidNum != null ? uidNum.longValue() : null;

                var principal = new AppPrincipal(uid, correo, role);
                var auth = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignored) {
                //No pasa nada si el token es inválido o expiró
            }
        }
        chain.doFilter(req, res);
    }
}
