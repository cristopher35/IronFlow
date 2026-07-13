package com.ironflow.equipmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class EquipoRequest {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Schema(example = "Caminadora Pro")
    private String nombre;

    @NotBlank(message = "La categoria es obligatoria")
    @Schema(example = "Cardio")
    private String categoria;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "(?i)DISPONIBLE|MANTENCION|INACTIVO", message = "El estado debe ser DISPONIBLE, MANTENCION o INACTIVO")
    @Schema(example = "DISPONIBLE")
    private String estado;

    @NotBlank(message = "La ubicacion es obligatoria")
    @Schema(example = "Sala 1")
    private String ubicacion;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    @Schema(example = "4")
    private Integer cantidadDisponible;

    @NotNull(message = "Debe indicar si requiere mantenimiento")
    @Schema(example = "false")
    private Boolean mantenimientoRequerido;
}
