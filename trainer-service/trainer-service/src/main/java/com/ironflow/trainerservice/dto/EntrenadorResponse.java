package com.ironflow.trainerservice.dto;

public record EntrenadorResponse(
        Long id,
        String nombre,
        String especialidad,
        Integer aniosExperiencia,
        String telefono,
        String correo,
        String estado,
        Boolean activo
) {
}
