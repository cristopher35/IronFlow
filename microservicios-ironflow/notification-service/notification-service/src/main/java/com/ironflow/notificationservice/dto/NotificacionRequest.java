package com.ironflow.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class NotificacionRequest {

    @NotNull(message = "El miembroId es obligatorio")
    @Positive(message = "El miembroId debe ser mayor a 0")
    @Schema(example = "1")
    private Long miembroId;

    @NotBlank(message = "El canal es obligatorio")
    @Pattern(regexp = "(?i)EMAIL|SMS|PUSH", message = "El canal debe ser EMAIL, SMS o PUSH")
    @Schema(example = "EMAIL")
    private String canal;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 255, message = "El asunto no puede superar 255 caracteres")
    @Schema(example = "Bienvenido a IronFlow")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 2000, message = "El mensaje no puede superar 2000 caracteres")
    @Schema(example = "Tu inscripción fue registrada correctamente")
    private String mensaje;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "(?i)PENDIENTE|ENVIADA|FALLIDA", message = "El estado debe ser PENDIENTE, ENVIADA o FALLIDA")
    @Schema(example = "PENDIENTE")
    private String estado;
}
