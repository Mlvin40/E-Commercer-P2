package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * The type Pedido detalle.
 */
@Entity @Table(name = "pedido_detalle")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PedidoDetalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // quién vende este producto (vendedor actual del producto)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "sitio_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal sitioFee;

    @Column(name = "ganancia_vendedor", nullable = false, precision = 10, scale = 2)
    private BigDecimal gananciaVendedor;
}
