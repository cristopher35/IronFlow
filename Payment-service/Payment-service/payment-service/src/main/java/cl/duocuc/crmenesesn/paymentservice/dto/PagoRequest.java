package cl.duocuc.crmenesesn.paymentservice.dto;

import jakarta.validation.constraints.*;

public record PagoRequest(

        @NotNull(message = "El ID del miembro es obligatorio")
        Long miembroId,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a 0")
        Double monto,

        @NotBlank(message = "El método de pago es obligatorio")
        @Pattern(regexp = "EFECTIVO|TARJETA|TRANSFERENCIA",
                message = "El método de pago debe ser EFECTIVO, TARJETA o TRANSFERENCIA")
        String metodoPago
) {}