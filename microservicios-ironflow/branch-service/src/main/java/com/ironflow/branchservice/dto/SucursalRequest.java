package com.ironflow.branchservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SucursalRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    @NotBlank(message = "El horario es obligatorio")
    private String horarioAtencion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
