package com.ironflow.trainerservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class EntrenadorRequest {

    @NotBlank(message = "El nombre del entrenador es obligatorio")
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "Los anios de experiencia son obligatorios")
    @PositiveOrZero(message = "Los anios de experiencia no pueden ser negativos")
    private Integer aniosExperiencia;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
