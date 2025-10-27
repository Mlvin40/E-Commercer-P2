package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The interface Producto repository.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Find by estado publicacion page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param pageable          the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacion(String estadoPublicacion, Pageable pageable);

    /**
     * Find by estado publicacion and categoria page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param categoria         the categoria
     * @param pageable          the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacionAndCategoria(
            String estadoPublicacion, String categoria, Pageable pageable);

    /**
     * Find by estado publicacion and nombre containing ignore case page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param q                 the q
     * @param pageable          the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacionAndNombreContainingIgnoreCase(
            String estadoPublicacion, String q, Pageable pageable);

    /**
     * Find by estado publicacion and vendedor id not page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param vendedorId        the vendedor id
     * @param pageable          the pageable
     * @return the page
     */
// si usas el catálogo privado (excluir mis productos)
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacionAndVendedor_IdNot(
            String estadoPublicacion, Long vendedorId, Pageable pageable);

    /**
     * Find by estado publicacion and categoria and vendedor id not page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param categoria         the categoria
     * @param vendedorId        the vendedor id
     * @param pageable          the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacionAndCategoriaAndVendedor_IdNot(
            String estadoPublicacion, String categoria, Long vendedorId, Pageable pageable);

    /**
     * Find by estado publicacion and nombre containing ignore case and vendedor id not page.
     *
     * @param estadoPublicacion the estado publicacion
     * @param q                 the q
     * @param vendedorId        the vendedor id
     * @param pageable          the pageable
     * @return the page
     */
    @EntityGraph(attributePaths = "vendedor")
    Page<Producto> findByEstadoPublicacionAndNombreContainingIgnoreCaseAndVendedor_IdNot(
            String estadoPublicacion, String q, Long vendedorId, Pageable pageable);


    /**
     * Find by vendedor id order by fecha publicacion desc list.
     *
     * @param vendedorId the vendedor id
     * @return the list
     */
    @EntityGraph(attributePaths = "vendedor")
    List<Producto> findByVendedor_IdOrderByFechaPublicacionDesc(Long vendedorId);

    /**
     * Buscar catalogo visible page.
     *
     * @param excludeVendedorId the exclude vendedor id
     * @param categoria         the categoria
     * @param q                 the q
     * @param pageable          the pageable
     * @return the page
     */
    @Query(
            value = """
    select p.*
    from productos p
    join usuarios v on v.id = p.usuario_id
    where p.estado_publicacion = 'APROBADO'
      and (:excludeVendedorId is null or v.id <> :excludeVendedorId)
      and (:categoria is null or p.categoria = :categoria)
      and (:q is null or p.nombre ilike concat('%', :q, '%'))
      and not exists (
        select 1 from sanciones s
        where s.usuario_id = v.id and s.estado = 'ACTIVA'
      )
    order by p.fecha_publicacion desc
    """,
            countQuery = """
    select count(*)
    from productos p
    join usuarios v on v.id = p.usuario_id
    where p.estado_publicacion = 'APROBADO'
      and (:excludeVendedorId is null or v.id <> :excludeVendedorId)
      and (:categoria is null or p.categoria = :categoria)
      and (:q is null or p.nombre ilike concat('%', :q, '%'))
      and not exists (
        select 1 from sanciones s
        where s.usuario_id = v.id and s.estado = 'ACTIVA'
      )
    """,
            nativeQuery = true
    )
    Page<Producto> buscarCatalogoVisible(
            @Param("excludeVendedorId") Long excludeVendedorId,
            @Param("categoria") String categoria,
            @Param("q") String q,
            Pageable pageable
    );

}