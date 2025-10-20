// src/main/java/ecommerce_api/ecommerce_api/service/CheckoutService.java
package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.CheckoutRequest;
import ecommerce_api.ecommerce_api.dto.CheckoutResponse;
import ecommerce_api.ecommerce_api.dto.TarjetaView;
import ecommerce_api.ecommerce_api.model.*;
import ecommerce_api.ecommerce_api.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class CheckoutService {

    private final CarritoRepository carritos;
    private final CarritoDetalleRepository detalles;
    private final ProductoRepository productos;
    private final PedidoRepository pedidos;
    private final PedidoDetalleRepository pedidoDetalles;
    private final TarjetaRepository tarjetas;
    private final PagoRepository pagos;

    @Transactional
    public CheckoutResponse pagar(Long userId, CheckoutRequest req) {
        Carrito carrito = carritos.findByUsuario_Id(userId)
                .orElseThrow(() -> new IllegalStateException("Carrito vacío"));
        List<CarritoDetalle> items = detalles.findByCarrito_Id(carrito.getId());
        if (items.isEmpty()) throw new IllegalStateException("Carrito vacío");

        for (CarritoDetalle d : items) {
            Producto p = d.getProducto();
            if (!"APROBADO".equals(p.getEstadoPublicacion()))
                throw new IllegalArgumentException("Producto no disponible: " + p.getNombre());
            if (d.getCantidad() > p.getStock())
                throw new IllegalArgumentException("Sin stock para: " + p.getNombre());
        }

        String token; String last4; String marca = "VISA";
        if (req.savedCardId() != null) {
            Tarjeta t = tarjetas.findById(req.savedCardId())
                    .orElseThrow(() -> new IllegalArgumentException("Tarjeta inválida"));
            if (!t.getUsuario().getId().equals(userId))
                throw new IllegalArgumentException("Tarjeta inválida");
            token = t.getToken(); last4 = t.getLast4();
        } else {
            if (req.numero() == null || req.numero().length() < 12)
                throw new IllegalArgumentException("Tarjeta inválida");
            last4 = req.numero().substring(req.numero().length() - 4);
            token = "tok_" + java.util.UUID.randomUUID();
        }

        // Usuario "referencia" para relaciones
        Usuario u = new Usuario();
        u.setId(userId);

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(u);
        pedido.setEstado("EN_CURSO");
        pedido.setFechaPedido(java.time.LocalDate.now());
        pedido.setFechaEstimadaEntrega(java.time.LocalDate.now().plusDays(5));
        pedido.setTotal(java.math.BigDecimal.ZERO);
        pedido = pedidos.save(pedido);

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        for (CarritoDetalle d : items) {
            Producto p = d.getProducto();
            int qty = d.getCantidad();
            java.math.BigDecimal unit = p.getPrecio();
            java.math.BigDecimal subtotal = unit.multiply(java.math.BigDecimal.valueOf(qty));
            java.math.BigDecimal fee = subtotal.multiply(new java.math.BigDecimal("0.05"))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            java.math.BigDecimal sellerGain = subtotal.subtract(fee);

            PedidoDetalle pd = new PedidoDetalle();
            pd.setPedido(pedido);
            pd.setProducto(p);
            pd.setVendedor(p.getVendedor());
            pd.setCantidad(qty);
            pd.setPrecioUnitario(unit);
            pd.setSubtotal(subtotal);
            pd.setSitioFee(fee);
            pd.setGananciaVendedor(sellerGain);
            pedidoDetalles.save(pd);

            p.setStock(p.getStock() - qty);
            productos.save(p);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedidos.save(pedido);

        // Guardar tarjeta (si aplica) usando la MISMA referencia 'u'
        if (Boolean.TRUE.equals(req.guardar()) && req.savedCardId() == null) {
            Tarjeta t = new Tarjeta();
            t.setUsuario(u);
            t.setToken(token);
            t.setLast4(last4);
            t.setMarca(marca);
            t.setTitular(req.titular());
            t.setPredeterminada(false);
            tarjetas.save(t);
        }

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(total);
        pago.setEstado("APROBADO");
        pago.setProveedorTxId("mock_" + java.util.UUID.randomUUID());
        pagos.save(pago);

        detalles.deleteAll(items);

        return new CheckoutResponse(pedido.getId(), total, "APROBADO");
    }

    @Transactional(readOnly = true)
    public List<TarjetaView> listarTarjetas(Long userId) {
        return tarjetas.findByUsuario_IdOrderByFechaRegistroDesc(userId)
                .stream()
                .map(t -> new TarjetaView(
                        t.getId(),
                        t.getLast4(),
                        t.getMarca(),
                        t.getTitular(),
                        Boolean.TRUE.equals(t.getPredeterminada())
                ))
                .toList();
    }
}
