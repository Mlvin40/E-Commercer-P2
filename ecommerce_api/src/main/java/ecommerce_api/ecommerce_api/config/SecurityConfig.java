package ecommerce_api.ecommerce_api.config;

import ecommerce_api.ecommerce_api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults()) // habilita CORS con la configuración del bean corsConfigurationSource
                .authorizeHttpRequests(auth -> auth
                        // preflight siempre permitido
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // público
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/files/**").permitAll()

                        // protegido por rol
                        .requestMatchers("/api/comun/**").hasRole("COMUN")
                        .requestMatchers("/api/moderador/**").hasAnyRole("MODERADOR","ADMIN")
                        .requestMatchers("/api/logistica/**").hasAnyRole("LOGISTICA","ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Para despliegue en Netlify, ngrok, localhost:4200 (Angular dev server)
        cfg.setAllowedOrigins(List.of(
                "https://ecommercemv.netlify.app",
                "http://localhost:4200",
                "https://hypercatalectic-donnetta-tubulously.ngrok-free.dev"

        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of(
                "Authorization","Content-Type","Accept","Origin",
                "X-Requested-With","ngrok-skip-browser-warning"
        ));
        cfg.setExposedHeaders(List.of("Authorization","Location"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

//    @Bean
//    public CorsFilter corsFilter(CorsConfigurationSource source) {
//        return new CorsFilter((UrlBasedCorsConfigurationSource) source);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
