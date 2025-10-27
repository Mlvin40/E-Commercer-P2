package ecommerce_api.ecommerce_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * The type Jwt service.
 */
@Service
public class JwtService {
    private final Key key;
    private final long expirationMs;
    private final long refreshExpirationMs;

    /**
     * Instantiates a new Jwt service.
     *
     * @param secret              the secret
     * @param expirationMs        the expiration ms
     * @param refreshExpirationMs the refresh expiration ms
     */
    // Inyecta las configuraciones desde application.properties
    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    /**
     * Generate token string.
     *
     * @param subject the subject
     * @param claims  the claims
     * @return the string
     */
    // Genera un token JWT con el sujeto y los reclamos proporcionados
    public String generateToken(String subject, Map<String,Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generate refresh token string.
     *
     * @param subject the subject
     * @return the string
     */
    // Genera un token de actualización JWT con el sujeto proporcionado
    public String generateRefreshToken(String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Parse jws.
     *
     * @param token the token
     * @return the jws
     */
    // Analiza y valida el token JWT, devolviendo los reclamos si es válido
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}