package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Role;
import ecommerce_api.ecommerce_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    //para la parte de sanciones y revisiones
    Page<Usuario> findByRolAndActivoTrueOrderByNombreAsc(Role rol, Pageable pageable);

    Page<Usuario> findByRolAndActivoTrueAndNombreContainingIgnoreCaseOrderByNombreAsc(
            Role rol, String nombre, Pageable pageable);
}