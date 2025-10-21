package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.reportes.TopClientePedidosRow;
import ecommerce_api.ecommerce_api.model.Pedido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportePedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
        select new ecommerce_api.ecommerce_api.dto.reportes.TopClientePedidosRow(
            u.id, u.nombre, u.correo,
            count(pe.id),
            coalesce(sum(pe.total), 0)
        )
        from Pedido pe
            join pe.usuario u
        where pe.fechaPedido >= :desde
          and pe.fechaPedido < :hasta
        group by u.id, u.nombre, u.correo
        order by count(pe.id) desc, sum(pe.total) desc
        """)
    List<TopClientePedidosRow> topClientesPorPedidos(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hastaExclusivo,
            Pageable pageable
    );
}