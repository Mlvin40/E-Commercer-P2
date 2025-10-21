package ecommerce_api.ecommerce_api.dto;

import ecommerce_api.ecommerce_api.model.Role;
import jakarta.validation.constraints.*;

public record ActualizarEmpleadoRequest(
        String nombre,
        String correo,
        String password,
        Boolean activo
) {}