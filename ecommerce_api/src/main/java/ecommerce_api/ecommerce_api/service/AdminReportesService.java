package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.reportes.*;
import ecommerce_api.ecommerce_api.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Admin reportes service.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportesService {

    private final ReportePedidoDetalleRepository pedidoDetalleRepo;
    private final ReportePedidoRepository pedidoRepo;
    private final ReporteProductoRepository productoRepo;

    //para el reprote de sanciones
    private final SancionRepository sanciones;

    /**
     * Top productos list.
     *
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param limit          the limit
     * @return the list
     */
    public List<TopProductoRow> topProductos(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topProductosMasVendidos(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Top vendedores ganancia list.
     *
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param limit          the limit
     * @return the list
     */
    public List<TopVendedorGananciaRow> topVendedoresGanancia(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topVendedoresGanancia(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Top clientes fee list.
     *
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param limit          the limit
     * @return the list
     */
    public List<TopClienteGananciaRow> topClientesFee(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topClientesGananciaSitio(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Top clientes ventas list.
     *
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param limit          the limit
     * @return the list
     */
   // rep3
    public List<TopVendedorUnidadesRow> topClientesVentas(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topVendedoresUnidades(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Top clientes pedidos list.
     *
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param limit          the limit
     * @return the list
     */
   // rep4
    public List<TopClientePedidosRow> topClientesPedidos(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoRepo.topClientesPorPedidos(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Top clientes productos venta list.
     *
     * @param limit the limit
     * @return the list
     */
   //rep5
    public List<TopClienteProductosVentaRow> topClientesProductosVenta(int limit) {
        return productoRepo.topClientesConMasProductosEnVenta(PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Historial page.
     *
     * @param estado         the estado
     * @param q              the q
     * @param desde          the desde
     * @param hastaInclusive the hasta inclusive
     * @param page           the page
     * @param size           the size
     * @return the page
     */

    //q es para buscar por nombre o correo del usuario sancionado
    public Page<SancionHistRow> historial(String estado, String q,
                                          LocalDate desde, LocalDate hastaInclusive,
                                          int page, int size) {
        LocalDate hastaEx = (hastaInclusive != null) ? hastaInclusive.plusDays(1) : null;
        var sort = Sort.by(Sort.Direction.DESC, "fecha").and(Sort.by(Sort.Direction.DESC, "id"));
        var pageable = PageRequest.of(Math.max(page,0), Math.max(size,1), sort);

        String e = (estado != null && !estado.isBlank()) ? estado.trim().toUpperCase() : null;

        // preparar patrón en Java
        String qLike = null;
        if (q != null && !q.isBlank()) {
            qLike = "%" + q.trim().toLowerCase() + "%";
        }

        return sanciones.historial(e, desde, hastaEx, qLike, pageable);
    }

}
