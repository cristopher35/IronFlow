package cl.duocuc.crmenesesn.ironflow.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record MiembroRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Schema(example = "Juan Pérez")
        String nombre,

        @NotBlank(message = "El RUT es obligatorio")
        @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]$", message = "El RUT debe tener el formato 12345678-9")
        @Schema(example = "12345678-9")
        String rut,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Schema(example = "juan.perez@correo.cl")
        String email,

        @Pattern(regexp = "^$|^[+]?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
        @Schema(example = "912345678")
        String telefono
) {}
