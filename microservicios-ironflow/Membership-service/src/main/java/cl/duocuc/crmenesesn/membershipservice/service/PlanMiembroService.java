package cl.duocuc.crmenesesn.membershipservice.service;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;

import java.util.List;

public interface PlanMiembroService {

    PlanMiembroResponse asignarPlan(PlanMiembroRequest request);
    PlanMiembroResponse obtenerPlanMiembroPorId(Long id);
    List<PlanMiembroResponse> obtenerTodosPlanMiembro();
    List<PlanMiembroResponse> obtenerPlanMiembroPorMiembroId(Long miembroId);
}