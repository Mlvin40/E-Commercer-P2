package ecommerce_api.ecommerce_api.controller.admin;

import ecommerce_api.ecommerce_api.dto.reportes.*;
import ecommerce_api.ecommerce_api.service.AdminReportesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportesController {

    private final AdminReportesService service;

    @GetMapping("/top-productos")
    public List<TopProductoRow> topProductos(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "10") int top
    ) {
        return service.topProductos(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    // a) Reutilizando la ruta "ganancia" para GANANCIA DE VENDEDORES
    @GetMapping("/top-clientes-ganancia")
    public List<TopVendedorGananciaRow> topClientesGananciaComoVendedores(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "5") int top
    ) {
        return service.topVendedoresGanancia(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    // b) (opcional) Alias explícito:
    @GetMapping("/top-vendedores-ganancia")
    public List<TopVendedorGananciaRow> topVendedoresGanancia(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "5") int top
    ) {
        return service.topVendedoresGanancia(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    @GetMapping("/top-clientes-fee")
    public List<TopClienteGananciaRow> topClientesFee(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "5") int top
    ) {
        return service.topClientesFee(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    //"Top 5 clientes que más productos han vendido"
    @GetMapping("/top-clientes-ventas")
    public List<TopVendedorUnidadesRow> topClientesVentas(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "5") int top
    ) {
        return service.topClientesVentas(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    // "Top 10 clientes que más pedidos han realizado"
    @GetMapping("/top-clientes-pedidos")
    public List<TopClientePedidosRow> topClientesPedidos(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "10") int top
    ) {
        return service.topClientesPedidos(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    @GetMapping("/top-clientes-productos-venta")
    public List<TopClienteProductosVentaRow> topClientesProductosVenta(
            @RequestParam(defaultValue = "10") int top
    ) {
        return service.topClientesProductosVenta(Math.min(Math.max(top, 1), 50));
    }

    @GetMapping("/historial-sanciones")
    public Page<SancionHistRow> historial(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.historial(estado, q, desde, hasta, page, size);
    }

}
