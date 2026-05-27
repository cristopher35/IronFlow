package com.ironflow.notificationservice.dto;

import java.time.LocalDateTime;

public record NotificacionResponse(
        Long id,
        Long miembroId,
        String canal,
        String asunto,
        String mensaje,
        String estado,
        LocalDateTime fechaEnvio
) {
}
