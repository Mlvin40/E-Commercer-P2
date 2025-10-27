package ecommerce_api.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * The type Pago.
 */
@Entity @Table(name = "pagos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 10)
    private String estado; //APROBADO, RECHAZADO

    @Column(name = "proveedor_tx_id", length = 100)
    private String proveedorTxId;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn = Instant.now();
}
