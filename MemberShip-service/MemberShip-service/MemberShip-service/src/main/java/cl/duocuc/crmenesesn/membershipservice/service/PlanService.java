package cl.duocuc.crmenesesn.membershipservice.service;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;

import java.util.List;

public interface PlanService {

    PlanResponse crearPlan(PlanRequest request);
    PlanResponse obtenerPlanPorId(Long id);
    List<PlanResponse> obtenerTodosLosPlanes();
    PlanResponse actualizarPlan(Long id, PlanRequest request);
    void eliminarPlan(Long id);
}