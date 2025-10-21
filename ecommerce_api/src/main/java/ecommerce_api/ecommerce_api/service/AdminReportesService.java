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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportesService {

    private final ReportePedidoDetalleRepository pedidoDetalleRepo;
    private final ReportePedidoRepository pedidoRepo;
    private final ReporteProductoRepository productoRepo;

    //para el reprote de sanciones
    private final SancionRepository sanciones;

    public List<TopProductoRow> topProductos(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topProductosMasVendidos(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    public List<TopVendedorGananciaRow> topVendedoresGanancia(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topVendedoresGanancia(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    public List<TopClienteGananciaRow> topClientesFee(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topClientesGananciaSitio(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    // rep3
    public List<TopVendedorUnidadesRow> topClientesVentas(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topVendedoresUnidades(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    // rep4
    public List<TopClientePedidosRow> topClientesPedidos(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoRepo.topClientesPorPedidos(desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    //rep5
    public List<TopClienteProductosVentaRow> topClientesProductosVenta(int limit) {
        return productoRepo.topClientesConMasProductosEnVenta(PageRequest.of(0, Math.max(1, limit)));
    }

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
