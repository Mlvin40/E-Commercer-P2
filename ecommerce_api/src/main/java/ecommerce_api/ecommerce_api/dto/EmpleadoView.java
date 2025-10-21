package ecommerce_api.ecommerce_api.dto;

import ecommerce_api.ecommerce_api.model.Role;

import java.time.Instant;

public record EmpleadoView(
        Long id, String nombre, String correo, Role rol, Boolean activo, Instant fechaCreacion
) {}