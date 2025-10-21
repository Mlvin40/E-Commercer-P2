package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.dto.reportes.SancionHistRow;
import ecommerce_api.ecommerce_api.model.Sancion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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


    //para el reporte de sanciones
    @Query(
            value = """
    select new ecommerce_api.ecommerce_api.dto.reportes.SancionHistRow(
        s.id, m.id, m.nombre, u.id, u.nombre, u.correo,
        s.motivo, s.fecha, s.estado
    )
    from Sancion s
      join s.moderador m
      join s.usuario u
    where (:estado is null or s.estado = :estado)
      and (:desde  is null or s.fecha >= :desde)
      and (:hasta  is null or s.fecha < :hasta)
      and (:qLike  is null or
           lower(u.nombre) like :qLike or
           lower(u.correo) like :qLike)
    order by s.fecha desc, s.id desc
    """,
            countQuery = """
    select count(s.id)
    from Sancion s
      join s.moderador m
      join s.usuario u
    where (:estado is null or s.estado = :estado)
      and (:desde  is null or s.fecha >= :desde)
      and (:hasta  is null or s.fecha < :hasta)
      and (:qLike  is null or
           lower(u.nombre) like :qLike or
           lower(u.correo) like :qLike)
    """
    )
    Page<SancionHistRow> historial(
            @Param("estado") String estado,
            @Param("desde")  LocalDate desde,
            @Param("hasta")  LocalDate hastaExclusivo,
            @Param("qLike")  String qLike,     // <- OJO: ahora es qLike
            Pageable pageable
    );

}
