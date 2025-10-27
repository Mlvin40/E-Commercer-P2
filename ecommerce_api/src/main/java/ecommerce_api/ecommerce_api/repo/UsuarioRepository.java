package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The interface Usuario repository.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Find by correo optional.
     *
     * @param correo the correo
     * @return the optional
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Exists by correo boolean.
     *
     * @param correo the correo
     * @return the boolean
     */
    boolean existsByCorreo(String correo);

    /**
     * Find by rol and activo true order by nombre asc page.
     *
     * @param rol      the rol
     * @param pageable the pageable
     * @return the page
     */
//para la parte de sanciones y revisiones
    Page<Usuario> findByRolAndActivoTrueOrderByNombreAsc(Role rol, Pageable pageable);

    /**
     * Find by rol and activo true and nombre containing ignore case order by nombre asc page.
     *
     * @param rol      the rol
     * @param nombre   the nombre
     * @param pageable the pageable
     * @return the page
     */
    Page<Usuario> findByRolAndActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(
            Role rol, String nombre, Pageable pageable);

    /**
     * Buscar empleados page.
     *
     * @param roles    the roles
     * @param q        the q
     * @param pageable the pageable
     * @return the page
     */
    @Query(
            value = """
    SELECT u.*
    FROM usuarios u
    WHERE u.rol IN (:roles)
      AND (:q IS NULL OR :q = '' 
           OR u.nombre ILIKE CONCAT('%', :q, '%') 
           OR u.correo ILIKE CONCAT('%', :q, '%'))
    ORDER BY u.nombre ASC, u.correo ASC
  """,
            countQuery = """
    SELECT COUNT(*)
    FROM usuarios u
    WHERE u.rol IN (:roles)
      AND (:q IS NULL OR :q = '' 
           OR u.nombre ILIKE CONCAT('%', :q, '%') 
           OR u.correo ILIKE CONCAT('%', :q, '%'))
  """,
            nativeQuery = true
    )
    Page<Usuario> buscarEmpleados(
            @Param("roles") java.util.List<String> roles,
            @Param("q") String q,
            Pageable pageable
    );

}