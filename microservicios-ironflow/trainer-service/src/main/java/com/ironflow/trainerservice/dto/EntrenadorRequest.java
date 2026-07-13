package com.ironflow.trainerservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class EntrenadorRequest {

    @NotBlank(message = "El nombre del entrenador es obligatorio")
    @Schema(example = "Ana Torres")
    private String nombre;

    @NotBlank(message = "La especialidad es obligatoria")
    @Schema(example = "Entrenamiento funcional")
    private String especialidad;

    @NotNull(message = "Los anios de experiencia son obligatorios")
    @PositiveOrZero(message = "Los anios de experiencia no pueden ser negativos")
    @Schema(example = "5")
    private Integer aniosExperiencia;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
    @Schema(example = "912345678")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Schema(example = "ana.torres@ironflow.cl")
    private String correo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "(?i)ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    @Schema(example = "ACTIVO")
    private String estado;
}
