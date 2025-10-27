package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.CalificacionView;
import ecommerce_api.ecommerce_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Calificacion repository.
 */
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    /**
     * Exists by usuario id and producto id boolean.
     *
     * @param usuarioId  the usuario id
     * @param productoId the producto id
     * @return the boolean
     */
    boolean existsByUsuario_IdAndProducto_Id(Long usuarioId, Long productoId);

    /**
     * Avg for product double.
     *
     * @param productoId the producto id
     * @return the double
     */
    @Query("select coalesce(avg(c.estrellas), 0) from Calificacion c where c.producto.id = :p")
    Double avgForProduct(@Param("p") Long productoId);

    /**
     * Count for product long.
     *
     * @param productoId the producto id
     * @return the long
     */
    @Query("select count(c) from Calificacion c where c.producto.id = :p")
    Long countForProduct(@Param("p") Long productoId);

    /**
     * Find views by producto id list.
     *
     * @param productoId the producto id
     * @return the list
     */
    // Esto lo que hace es devolver una lista de CalificacionView (DTO) en lugar de entidades Calificacion completas
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