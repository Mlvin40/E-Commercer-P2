package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * The type Calificacion.
 */
@Entity @Table(name = "calificaciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id","producto_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Calificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="producto_id", nullable=false)
    private Producto producto;

    @Column(nullable=false)
    private Integer estrellas; // 1-5

    @Column(columnDefinition="text")
    private String comentario;

    @Column(nullable=false)
    private Instant fecha = Instant.now();
}
