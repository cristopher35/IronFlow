package com.ironflow.equipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EquipoRequest {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "(?i)DISPONIBLE|MANTENCION|INACTIVO", message = "El estado debe ser DISPONIBLE, MANTENCION o INACTIVO")
    private String estado;

    @NotBlank(message = "La ubicacion es obligatoria")
    private String ubicacion;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    private Integer cantidadDisponible;

    @NotNull(message = "Debe indicar si requiere mantenimiento")
    private Boolean mantenimientoRequerido;
}
