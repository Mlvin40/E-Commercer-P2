package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * The type Producto revision.
 */
@Entity @Table(name = "producto_revision")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoRevision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(optional = false) @JoinColumn(name = "solicitado_por")
    private Usuario solicitadoPor; //Vendedor normalmente

    @Column(nullable = false)
    private String estado; // PENDIENTE, APROBADO, RECHAZADO

    @ManyToOne @JoinColumn(name = "moderador_id")
    private Usuario moderador;

    private String comentario;

    @Column(name = "creado_en")
    private Instant creadoEn;

    @Column(name = "resuelto_en")
    private Instant resueltoEn;
}
