package ecommerce_api.ecommerce_api.config;

import ecommerce_api.ecommerce_api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

/**
 * The type Security config.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // auth público
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        // catálogo público
                        .requestMatchers(HttpMethod.GET,"/api/public/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/files/**").permitAll()        // ver imágenes
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").authenticated() // subir requiere login
                        // zonas por rol
                        .requestMatchers("/api/comun/**").hasRole("COMUN")
                        .requestMatchers("/api/moderador/**").hasAnyRole("MODERADOR","ADMIN")
                        .requestMatchers("/api/logistica/**").hasAnyRole("LOGISTICA","ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // todo lo demás requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /**
     * Cors configuration source cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Para despliegue en Netlify, ngrok, localhost:4200 (Angular dev server)
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost:4200",
                "https://*.netlify.app",
                "https://*.ngrok-free.app",
                "https://*.ngrok.app"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
