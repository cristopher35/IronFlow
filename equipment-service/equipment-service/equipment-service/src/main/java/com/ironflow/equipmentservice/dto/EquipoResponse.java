package com.ironflow.equipmentservice.dto;

public record EquipoResponse(
        Long id,
        String nombre,
        String categoria,
        String estado,
        String ubicacion,
        Integer cantidadDisponible,
        Boolean mantenimientoRequerido
) {
}
