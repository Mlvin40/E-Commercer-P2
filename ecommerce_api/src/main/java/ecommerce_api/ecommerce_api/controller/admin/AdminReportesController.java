package ecommerce_api.ecommerce_api.controller.admin;

import ecommerce_api.ecommerce_api.dto.reportes.TopClienteGananciaRow;
import ecommerce_api.ecommerce_api.dto.reportes.TopProductoRow;
import ecommerce_api.ecommerce_api.service.AdminReportesService;
import lombok.RequiredArgsConstructor;
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
            @RequestParam LocalDate hasta,                 // inclusivo desde el cliente
            @RequestParam(defaultValue = "10") int top
    ) {
        return service.topProductos(desde, hasta, Math.min(Math.max(top, 1), 50));
    }

    @GetMapping("/top-clientes-ganancia")
    public List<TopClienteGananciaRow> topClientesGanancia(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,                 // inclusivo desde el cliente
            @RequestParam(defaultValue = "5") int top
    ) {
        return service.topClientesGanancia(desde, hasta, Math.min(Math.max(top, 1), 50));
    }
}
