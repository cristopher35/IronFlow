package cl.duocuc.crmenesesn.ironflow.dto;

import jakarta.validation.constraints.*;

public record MiembroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "El RUT debe tener el formato 12345678-9")
        String rut,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @Pattern(regexp = "^$|^[+]?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
        String telefono
) {}
