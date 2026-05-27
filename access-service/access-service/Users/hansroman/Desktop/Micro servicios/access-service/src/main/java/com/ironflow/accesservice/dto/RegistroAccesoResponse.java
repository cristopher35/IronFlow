package com.ironflow.accesservice.dto;

import java.time.LocalDateTime;

public record RegistroAccesoResponse(
        Long id,
        Long miembroId,
        String estado,
        LocalDateTime fechaAcceso,
        String observacion
) {
}
