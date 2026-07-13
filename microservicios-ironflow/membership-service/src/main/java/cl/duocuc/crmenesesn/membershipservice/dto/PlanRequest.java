package cl.duocuc.crmenesesn.membershipservice.dto;

import jakarta.validation.constraints.*;

public record PlanRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        Double precio,

        @NotNull(message = "La duración es obligatoria")
        @Positive(message = "La duración debe ser mayor a 0")
        Integer diasDuracion
) {}