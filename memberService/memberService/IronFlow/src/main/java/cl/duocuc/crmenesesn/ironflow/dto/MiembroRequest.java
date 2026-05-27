package cl.duocuc.crmenesesn.ironflow.dto;

import jakarta.validation.constraints.*;

public record MiembroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El RUT es obligatorio")
        String rut,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        String telefono
) {}