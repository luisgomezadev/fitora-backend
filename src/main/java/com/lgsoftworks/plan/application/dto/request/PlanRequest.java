package com.lgsoftworks.plan.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanRequest {

    @NotBlank(message = "El nombre del plan es requerido")
    @Size(min = 3, max = 100,
            message = "El nombre del plan debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 500,
            message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "La duración del plan es requerida")
    @Min(value = 1, message = "La duración del plan debe ser mayor a cero")
    private Integer durationDays;

    @NotNull(message = "El precio del plan es requerido")
    @DecimalMin(value = "0.0", message = "El precio del plan no puede ser negativo")
    private BigDecimal price;
}