package cl.duocuc.crmenesesn.membershipservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PlanMiembroRequest(

        @NotNull(message = "El ID del miembro es obligatorio")
        Long miembroId,

        @NotNull(message = "El ID del plan es obligatorio")
        Long planId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio
) {}