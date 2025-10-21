package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SancionRepository extends JpaRepository<Sancion, Long> {

    @Query("""
        select s from Sancion s
        join fetch s.usuario u
        join fetch s.moderador m
        where (:usuarioId is null or u.id = :usuarioId)
          and (:estado is null or s.estado = :estado)
        order by s.fecha desc, s.id desc
    """)
    List<Sancion> buscar(@Param("usuarioId") Long usuarioId,
                         @Param("estado") String estado);

    @Query("select count(s) > 0 from Sancion s where s.usuario.id = :usuarioId and s.estado = 'ACTIVA'")
    boolean existeActivaPara(@Param("usuarioId") Long usuarioId);
}
