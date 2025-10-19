package ecommerce_api.ecommerce_api.service;

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

import java.time.Instant;

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
}
