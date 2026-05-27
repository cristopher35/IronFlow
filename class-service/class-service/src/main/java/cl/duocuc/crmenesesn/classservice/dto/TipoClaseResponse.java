package cl.duocuc.crmenesesn.classservice.dto;

public record TipoClaseResponse(
        Long id,
        String nombre,
        String descripcion,
        String estado
) {}