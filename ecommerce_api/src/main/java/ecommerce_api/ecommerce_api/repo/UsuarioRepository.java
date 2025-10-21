package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    //para la parte de sanciones y revisiones
    Page<Usuario> findByRolAndActivoTrueOrderByNombreAsc(Role rol, Pageable pageable);

    Page<Usuario> findByRolAndActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(
            Role rol, String nombre, Pageable pageable);

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