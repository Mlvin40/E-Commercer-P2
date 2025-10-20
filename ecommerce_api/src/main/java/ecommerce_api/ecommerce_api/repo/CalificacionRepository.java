package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.CalificacionView;
import ecommerce_api.ecommerce_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByUsuario_IdAndProducto_Id(Long usuarioId, Long productoId);

    @Query("select coalesce(avg(c.estrellas), 0) from Calificacion c where c.producto.id = :p")
    Double avgForProduct(@Param("p") Long productoId);

    @Query("select count(c) from Calificacion c where c.producto.id = :p")
    Long countForProduct(@Param("p") Long productoId);

    // ⬇️ Nuevo: proyección directa al record CalificacionView
    @Query("""
           select new ecommerce_api.ecommerce_api.dto.CalificacionView(
               u.nombre, c.estrellas, c.comentario, c.fecha
           )
           from Calificacion c
           join c.usuario u
           where c.producto.id = :p
           order by c.fecha desc
           """)
    List<CalificacionView> findViewsByProductoId(@Param("p") Long productoId);
}