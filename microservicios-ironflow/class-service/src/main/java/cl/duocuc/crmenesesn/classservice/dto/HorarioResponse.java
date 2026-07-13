package cl.duocuc.crmenesesn.classservice.dto;

import java.time.LocalDateTime;

public record HorarioResponse(
        Long id,
        TipoClaseResponse tipoClase,
        Long entrenadorId,
        LocalDateTime fechaHora,
        Integer aforoMax,
        Integer aforoActual,
        String estado
) {}