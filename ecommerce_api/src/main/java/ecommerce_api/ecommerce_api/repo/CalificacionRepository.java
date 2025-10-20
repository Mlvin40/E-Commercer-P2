package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByUsuario_IdAndProducto_Id(Long usuarioId, Long productoId);

    @Query("select coalesce(avg(c.estrellas), 0) from Calificacion c where c.producto.id = :p")
    Double avgForProduct(@Param("p") Long productoId);

    @Query("select count(c) from Calificacion c where c.producto.id = :p")
    Long countForProduct(@Param("p") Long productoId);
}
