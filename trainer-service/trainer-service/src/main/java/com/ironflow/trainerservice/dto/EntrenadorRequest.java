package com.ironflow.trainerservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "(?i)ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private String estado;
}
