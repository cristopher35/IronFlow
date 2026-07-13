package cl.duocuc.crmenesesn.membershipservice.controller;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.service.PlanMiembroService;
import cl.duocuc.crmenesesn.membershipservice.service.PlanService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MembershipControllersTest {

    private final PlanService planService = mock(PlanService.class);
    private final PlanMiembroService planMiembroService = mock(PlanMiembroService.class);
    private final PlanController planController = new PlanController(planService);
    private final PlanMiembroController planMiembroController = new PlanMiembroController(planMiembroService);

    @Test
    void planControllerDelegatesCrudEndpoints() {
        // Given
        PlanRequest request = new PlanRequest("Full", 29990.0, 30);
        PlanResponse response = new PlanResponse(1L, "Full", 29990.0, 30, "ACTIVO");
        when(planService.crearPlan(request)).thenReturn(response);
        when(planService.obtenerPlanPorId(1L)).thenReturn(response);
        when(planService.obtenerTodosLosPlanes()).thenReturn(List.of(response));
        when(planService.actualizarPlan(1L, request)).thenReturn(response);

        // When / Then
        assertEquals(201, planController.crearPlan(request).getStatusCode().value());
        assertEquals(response, planController.obtenerPlanPorId(1L).getBody());
        assertEquals(List.of(response), planController.obtenerTodosLosPlanes().getBody());
        assertEquals(response, planController.actualizarPlan(1L, request).getBody());
        assertEquals(204, planController.eliminarPlan(1L).getStatusCode().value());
        verify(planService).eliminarPlan(1L);
    }

    @Test
    void planMiembroControllerDelegatesReadAndAssignEndpoints() {
        // Given
        LocalDate inicio = LocalDate.now();
        PlanMiembroRequest request = new PlanMiembroRequest(7L, 1L, inicio);
        PlanResponse plan = new PlanResponse(1L, "Full", 29990.0, 30, "ACTIVO");
        PlanMiembroResponse response = new PlanMiembroResponse(
                10L, 7L, plan, inicio, inicio.plusDays(30), "ACTIVO", LocalDateTime.now());
        when(planMiembroService.asignarPlan(request)).thenReturn(response);
        when(planMiembroService.obtenerPlanMiembroPorId(10L)).thenReturn(response);
        when(planMiembroService.obtenerTodosPlanMiembro()).thenReturn(List.of(response));
        when(planMiembroService.obtenerPlanMiembroPorMiembroId(7L)).thenReturn(List.of(response));

        // When / Then
        assertEquals(201, planMiembroController.asignarPlan(request).getStatusCode().value());
        assertEquals(response, planMiembroController.obtenerPlanMiembroPorId(10L).getBody());
        assertEquals(List.of(response), planMiembroController.obtenerTodosPlanMiembro().getBody());
        assertEquals(List.of(response), planMiembroController.obtenerPlanMiembroPorMiembroId(7L).getBody());
    }
}
