package cl.duocuc.crmenesesn.bookingservice.dto;

import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        Long horarioId,
        Long miembroId,
        LocalDateTime fechaReserva,
        String estado
) {}