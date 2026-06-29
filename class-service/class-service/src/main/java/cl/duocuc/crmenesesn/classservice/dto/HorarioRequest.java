package cl.duocuc.crmenesesn.classservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public record HorarioRequest(

        @NotNull(message = "El ID del tipo de clase es obligatorio")
        @Positive(message = "El ID del tipo de clase debe ser mayor a 0")
        @Schema(example = "1")
        Long tipoClaseId,

        @NotNull(message = "El ID del entrenador es obligatorio")
        @Positive(message = "El ID del entrenador debe ser mayor a 0")
        @Schema(example = "2")
        Long entrenadorId,

        @NotNull(message = "La fecha y hora es obligatoria")
        @Future(message = "La fecha y hora debe ser futura")
        @Schema(example = "2026-12-20T18:30:00")
        LocalDateTime fechaHora,

        @NotNull(message = "El aforo máximo es obligatorio")
        @Positive(message = "El aforo máximo debe ser mayor a 0")
        @Schema(example = "20")
        Integer aforoMax
) {}
