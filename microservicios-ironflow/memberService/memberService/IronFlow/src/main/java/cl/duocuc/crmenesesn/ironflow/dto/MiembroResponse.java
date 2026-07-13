package cl.duocuc.crmenesesn.ironflow.dto;

public record MiembroResponse(
        Long id,
        String nombre,
        String rut,
        String email,
        String telefono,
        String estado
) {}