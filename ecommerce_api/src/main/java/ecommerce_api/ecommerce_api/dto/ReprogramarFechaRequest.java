package ecommerce_api.ecommerce_api.dto;

import java.time.LocalDate;

/**
 * The type Reprogramar fecha request.
 */
public record ReprogramarFechaRequest(LocalDate fechaEstimadaEntrega) {}