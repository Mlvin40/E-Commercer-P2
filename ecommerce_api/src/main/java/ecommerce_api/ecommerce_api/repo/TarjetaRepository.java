package ecommerce_api.ecommerce_api.repo;

import ecommerce_api.ecommerce_api.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByUsuario_IdOrderByFechaRegistroDesc(Long usuarioId);
}