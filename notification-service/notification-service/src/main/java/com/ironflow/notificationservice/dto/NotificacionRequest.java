package com.ironflow.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionRequest {

    @NotNull(message = "El miembroId es obligatorio")
    private Long miembroId;

    @NotBlank(message = "El canal es obligatorio")
    private String canal;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
