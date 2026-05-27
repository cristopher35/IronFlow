package cl.duocuc.crmenesesn.membershipservice.dto;

public record PlanResponse(
        Long id,
        String nombre,
        Double precio,
        Integer diasDuracion,
        String estado
) {}