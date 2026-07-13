package cl.duocuc.crmenesesn.bookingservice.dto;

import jakarta.validation.constraints.*;

public record ReservaRequest(

        @NotNull(message = "El ID del horario es obligatorio")
        Long horarioId,

        @NotNull(message = "El ID del miembro es obligatorio")
        Long miembroId
) {}