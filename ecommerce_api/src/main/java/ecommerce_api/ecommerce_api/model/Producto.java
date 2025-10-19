package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="productos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120) private String nombre;
    @Column(columnDefinition="text") private String descripcion;
    @Column(name="imagen_url") private String imagenUrl;

    @Column(nullable=false, precision=10, scale=2) private BigDecimal precio;
    @Column(nullable=false) private Integer stock;

    @Column(nullable=false, length=10) private String estado;           // NUEVO/USADO
    @Column(nullable=false, length=20) private String categoria;        // TECNOLOGIA...

    @Column(name="estado_publicacion", nullable=false, length=12)
    private String estadoPublicacion; // PENDIENTE/APROBADO/RECHAZADO

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="usuario_id", nullable=false)
    private Usuario vendedor;

    @Column(name="fecha_publicacion", nullable=false)
    private Instant fechaPublicacion;
}
