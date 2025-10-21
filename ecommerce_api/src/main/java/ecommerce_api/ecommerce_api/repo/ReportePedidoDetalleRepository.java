// src/main/java/ecommerce_api/ecommerce_api/repo/ReportePedidoDetalleRepository.java
package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.reportes.TopClienteGananciaRow;
import ecommerce_api.ecommerce_api.dto.reportes.TopProductoRow;
import ecommerce_api.ecommerce_api.dto.reportes.TopVendedorGananciaRow;
import ecommerce_api.ecommerce_api.dto.reportes.TopVendedorUnidadesRow;
import ecommerce_api.ecommerce_api.model.PedidoDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportePedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    @Query("""
        select new ecommerce_api.ecommerce_api.dto.reportes.TopProductoRow(
            p.id, p.nombre,
            sum(pd.cantidad), sum(pd.subtotal)
        )
        from PedidoDetalle pd
            join pd.pedido pe
            join pd.producto p
        where pe.estado = 'ENTREGADO'
          and pe.fechaPedido >= :desde
          and pe.fechaPedido < :hasta
        group by p.id, p.nombre
        order by sum(pd.cantidad) desc, sum(pd.subtotal) desc
        """)
    List<TopProductoRow> topProductosMasVendidos(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hastaExclusivo,
            Pageable pageable
    );

    // NUEVO: top vendedores por ganancia (sum(ganancia_vendedor))
    @Query("""
        select new ecommerce_api.ecommerce_api.dto.reportes.TopVendedorGananciaRow(
            v.id, v.nombre, v.correo,
            sum(pd.gananciaVendedor),
            sum(pd.cantidad),
            sum(pd.subtotal)
        )
        from PedidoDetalle pd
            join pd.pedido pe
            join pd.vendedor v
        where pe.estado = 'ENTREGADO'
          and pe.fechaPedido >= :desde
          and pe.fechaPedido < :hasta
        group by v.id, v.nombre, v.correo
        order by sum(pd.gananciaVendedor) desc, sum(pd.subtotal) desc
        """)
    List<TopVendedorGananciaRow> topVendedoresGanancia(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hastaExclusivo,
            Pageable pageable
    );


    @Query("""
    select new ecommerce_api.ecommerce_api.dto.reportes.TopClienteGananciaRow(
        u.id, u.nombre, u.correo,
        sum(pe.total),
        sum(pd.sitioFee)
    )
    from PedidoDetalle pd
        join pd.pedido pe
        join pe.usuario u
    where pe.estado = 'ENTREGADO'
      and pe.fechaPedido >= :desde
      and pe.fechaPedido < :hasta
    group by u.id, u.nombre, u.correo
    order by sum(pd.sitioFee) desc, sum(pe.total) desc
    """)
    List<TopClienteGananciaRow> topClientesGananciaSitio(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hastaExclusivo,
            Pageable pageable
    );


    //Top por **unidades vendidas** en el periodo
    @Query("""
        select new ecommerce_api.ecommerce_api.dto.reportes.TopVendedorUnidadesRow(
            v.id, v.nombre, v.correo,
            sum(pd.cantidad),
            count(distinct pe.id),
            sum(pd.subtotal)
        )
        from PedidoDetalle pd
            join pd.pedido pe
            join pd.vendedor v
        where pe.estado = 'ENTREGADO'
          and pe.fechaPedido >= :desde
          and pe.fechaPedido < :hasta
        group by v.id, v.nombre, v.correo
        order by sum(pd.cantidad) desc, sum(pd.subtotal) desc
        """)
    List<TopVendedorUnidadesRow> topVendedoresUnidades(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hastaExclusivo,
            Pageable pageable
    );

}
