package ecommerce_api.ecommerce_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * The type Tarjeta.
 */
@Entity @Table(name = "tarjetas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Tarjeta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore // Evita ciclos infinitos en serialización JSON
    private Usuario usuario;

    @Column(nullable = false, length = 120)
    private String token;

    @Column(nullable = false, length = 4)
    private String last4;

    private String marca;
    @Column(name = "exp_mes")  private Short expMes;
    @Column(name = "exp_anio") private Short expAnio;

    private String titular;

    @Column(nullable = false)
    private Boolean predeterminada = Boolean.FALSE;

    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro = Instant.now();
}
