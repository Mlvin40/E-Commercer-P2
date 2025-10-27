package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * The type Carrito detalle.
 */
@Entity @Table(name = "carrito_detalle",
        uniqueConstraints = @UniqueConstraint(name = "uq_item_unico", columnNames = {"carrito_id","producto_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CarritoDetalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}
