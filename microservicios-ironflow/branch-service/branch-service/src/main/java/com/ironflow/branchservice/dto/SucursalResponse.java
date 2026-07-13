package com.ironflow.branchservice.dto;

public record SucursalResponse(
        Long id,
        String nombre,
        String ciudad,
        String direccion,
        String telefono,
        String horarioAtencion,
        String estado,
        Boolean activa
) {
}
