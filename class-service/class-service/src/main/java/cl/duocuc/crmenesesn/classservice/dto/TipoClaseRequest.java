package cl.duocuc.crmenesesn.classservice.dto;

import jakarta.validation.constraints.*;

public record TipoClaseRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion
) {}