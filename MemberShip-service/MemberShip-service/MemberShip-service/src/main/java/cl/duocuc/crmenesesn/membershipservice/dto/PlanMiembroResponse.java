package cl.duocuc.crmenesesn.membershipservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PlanMiembroResponse(
        Long id,
        Long miembroId,
        PlanResponse plan,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        LocalDateTime creadoEn
) {}