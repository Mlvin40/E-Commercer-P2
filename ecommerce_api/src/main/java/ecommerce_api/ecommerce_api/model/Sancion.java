package ecommerce_api.ecommerce_api.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * The type Sancion.
 */

@Entity @Table(name = "sanciones")
@Getter @Setter
public class Sancion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "moderador_id")
    private Usuario moderador;

    @ManyToOne(optional = false) @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String motivo;

    @Column(columnDefinition = "date default current_date")
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false)
    private String estado = "ACTIVA"; // ACTIVA | LEVANTADA
}
