package ecommerce_api.ecommerce_api.service;

import ecommerce_api.ecommerce_api.dto.ProductoMineView;
import ecommerce_api.ecommerce_api.dto.ProductoUpsertDto;
import ecommerce_api.ecommerce_api.model.Producto;
import ecommerce_api.ecommerce_api.model.ProductoRevision;
import ecommerce_api.ecommerce_api.model.Usuario;
import ecommerce_api.ecommerce_api.repo.ProductoRepository;
import ecommerce_api.ecommerce_api.repo.ProductoRevisionRepository;
import ecommerce_api.ecommerce_api.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productos;
    private final UsuarioRepository usuarios;
    private final ProductoRevisionRepository revisiones;

    @Transactional
    public Producto crear(Long vendedorId, ProductoUpsertDto dto) {
        // 0) Dueño
        Usuario vendedor = usuarios.findById(vendedorId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 1) Validaciones
        if (dto.nombre() == null || dto.nombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.precio() == null || dto.precio().doubleValue() <= 0)
            throw new IllegalArgumentException("El precio debe ser > 0");
        if (dto.stock() == null || dto.stock() < 1)
            throw new IllegalArgumentException("El stock mínimo es 1");
        if (dto.estado() == null || dto.categoria() == null)
            throw new IllegalArgumentException("Estado y categoría son obligatorios");

        String estado = dto.estado().trim().toUpperCase();
        String categoria = dto.categoria().trim().toUpperCase();

        // 2) Insert producto
        Producto p = Producto.builder()
                .nombre(dto.nombre().trim())
                .descripcion(dto.descripcion())
                .imagenUrl(dto.imagenUrl())
                .precio(dto.precio())
                .stock(dto.stock())
                .estado(estado)
                .categoria(categoria)
                .estadoPublicacion("PENDIENTE") // entra a revisión
                .vendedor(vendedor)
                .fechaPublicacion(Instant.now())
                .build();

        p = productos.save(p);

        // 3) Insert revisión PENDIENTE (evita duplicados pend.)
        if (!revisiones.existsByProductoAndEstado(p, "PENDIENTE")) {
            var rev = ProductoRevision.builder()
                    .producto(p)
                    .solicitadoPor(vendedor)
                    .estado("PENDIENTE")
                    .creadoEn(Instant.now())
                    .build();
            revisiones.save(rev);
        }

        return p;
    }

    @Transactional
    public void actualizar(Long vendedorId, Long productoId, ProductoUpsertDto dto) {
        var p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto"));

        if (!p.getVendedor().getId().equals(vendedorId)) {
            throw new IllegalArgumentException("No puedes modificar este producto");
        }

        if (dto.nombre() == null || dto.nombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (dto.precio() == null || dto.precio().doubleValue() <= 0)
            throw new IllegalArgumentException("El precio debe ser > 0");
        if (dto.stock() == null || dto.stock() < 1)
            throw new IllegalArgumentException("El stock mínimo es 1");
        if (dto.estado() == null || dto.categoria() == null)
            throw new IllegalArgumentException("Estado y categoría son obligatorios");

        p.setNombre(dto.nombre().trim());
        p.setDescripcion(dto.descripcion());

        // <- SOLO si recibimos una nueva URL (por upload), la aplicamos
        if (dto.imagenUrl() != null && !dto.imagenUrl().isBlank()) {
            p.setImagenUrl(dto.imagenUrl().trim());
        }

        p.setPrecio(dto.precio());
        p.setStock(dto.stock());
        p.setEstado(dto.estado().trim().toUpperCase());
        p.setCategoria(dto.categoria().trim().toUpperCase());

        // Entra a revisión cada actualización
        p.setEstadoPublicacion("PENDIENTE");
        // p.setFechaPublicacion(Instant.now()); // opcional si quieres “subirlo” en listados

        productos.save(p);

        // Crea registro de revisión pendiente si no hay uno ya
        if (!revisiones.existsByProductoAndEstado(p, "PENDIENTE")) {
            var rev = ProductoRevision.builder()
                    .producto(p)
                    .solicitadoPor(p.getVendedor())
                    .estado("PENDIENTE")
                    .creadoEn(Instant.now())
                    .build();
            revisiones.save(rev);
        }
    }

    @Transactional
    public void eliminar(Long vendedorId, Long productoId) {
        var p = productos.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto"));
        if (!p.getVendedor().getId().equals(vendedorId)) {
            throw new IllegalArgumentException("No puedes eliminar este producto");
        }
        productos.delete(p); // on delete cascade eliminará revisiones
    }

    @Transactional(readOnly = true)
    public List<ProductoMineView> listarMisProductos(Long vendedorId) {
        return productos.findByVendedor_IdOrderByFechaPublicacionDesc(vendedorId)
                .stream()
                .map(p -> new ProductoMineView(
                        p.getId(),
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getImagenUrl(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getEstado(),
                        p.getCategoria(),
                        p.getEstadoPublicacion(),
                        p.getFechaPublicacion()
                ))
                .collect(Collectors.toList());
    }


}
