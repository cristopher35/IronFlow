package cl.duocuc.crmenesesn.classservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record HorarioRequest(

        @NotNull(message = "El ID del tipo de clase es obligatorio")
        Long tipoClaseId,

        @NotNull(message = "El ID del entrenador es obligatorio")
        Long entrenadorId,

        @NotNull(message = "La fecha y hora es obligatoria")
        @Future(message = "La fecha y hora debe ser futura")
        LocalDateTime fechaHora,

        @NotNull(message = "El aforo máximo es obligatorio")
        @Positive(message = "El aforo máximo debe ser mayor a 0")
        Integer aforoMax
) {}