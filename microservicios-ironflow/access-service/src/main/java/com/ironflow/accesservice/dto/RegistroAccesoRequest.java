package com.ironflow.accesservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAccesoRequest {

    @NotNull(message = "El miembroId es obligatorio")
    private Long miembroId;

    private String observacion;
}