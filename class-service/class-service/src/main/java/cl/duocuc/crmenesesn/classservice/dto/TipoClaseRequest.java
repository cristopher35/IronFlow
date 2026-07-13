package cl.duocuc.crmenesesn.classservice.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

public record TipoClaseRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Schema(example = "Yoga")
        String nombre,

        @Schema(example = "Clase de movilidad y respiración") String descripcion
) {}
