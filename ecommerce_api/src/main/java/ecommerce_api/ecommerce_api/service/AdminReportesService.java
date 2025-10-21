package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.reportes.TopClienteGananciaRow;
import ecommerce_api.ecommerce_api.dto.reportes.TopProductoRow;
import ecommerce_api.ecommerce_api.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportesService {

    private final ReportePedidoDetalleRepository pedidoDetalleRepo;

    public List<TopProductoRow> topProductos(LocalDate desde, LocalDate hastaInclusive, int limit) {
        // hacemos hasta exclusivo para cubrir todo el día final
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topProductosMasVendidos(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }

    public List<TopClienteGananciaRow> topClientesGanancia(LocalDate desde, LocalDate hastaInclusive, int limit) {
        LocalDate hastaExclusivo = hastaInclusive.plusDays(1);
        return pedidoDetalleRepo.topClientesGanancia(
                desde, hastaExclusivo, PageRequest.of(0, Math.max(1, limit)));
    }
}
