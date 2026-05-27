package cl.duocuc.crmenesesn.paymentservice.dto;

import java.time.LocalDateTime;

public record PagoResponse(
        Long id,
        Long miembroId,
        Double monto,
        LocalDateTime fechaPago,
        String metodoPago,
        String estado
) {}