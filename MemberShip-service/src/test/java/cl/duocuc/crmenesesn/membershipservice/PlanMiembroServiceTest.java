package cl.duocuc.crmenesesn.membershipservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

import cl.duocuc.crmenesesn.membershipservice.client.MemberClient;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;
import cl.duocuc.crmenesesn.membershipservice.model.Plan;
import cl.duocuc.crmenesesn.membershipservice.model.PlanMiembro;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanMiembroRepository;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanRepository;
import cl.duocuc.crmenesesn.membershipservice.service.PlanMiembroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
public class PlanMiembroServiceTest {

    @Autowired
    private PlanMiembroService planMiembroService;

    @MockitoBean
    private PlanMiembroRepository planMiembroRepository;

    @MockitoBean
    private PlanRepository planRepository;

    @MockitoBean
    private MemberClient memberClient;

    @Test
    @DisplayName("Debe asignar un plan correctamente cuando todo es válido")
    public void testAsignarPlan() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        PlanMiembroRequest request = new PlanMiembroRequest(1L, 1L, LocalDate.of(2026, 1, 1));
        PlanMiembro planMiembro = PlanMiembro.builder()
                .id(1L).miembroId(1L).plan(plan)
                .fechaInicio(LocalDate.of(2026, 1, 1))
                .fechaFin(LocalDate.of(2026, 1, 31))
                .estado("ACTIVO").creadoEn(LocalDateTime.now()).build();

        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planMiembroRepository.existsByMiembroIdAndEstado(1L, "ACTIVO")).thenReturn(false);
        when(planMiembroRepository.save(any(PlanMiembro.class))).thenReturn(planMiembro);

        // WHEN
        PlanMiembroResponse result = planMiembroService.asignarPlan(request);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.miembroId());
        assertEquals("ACTIVO", result.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el miembro no existe en member-service")
    public void testAsignarPlanMiembroNoExiste() {
        // GIVEN
        PlanMiembroRequest request = new PlanMiembroRequest(99L, 1L, LocalDate.of(2026, 1, 1));
        when(memberClient.getMemberById(99L)).thenThrow(new RuntimeException("404 Not Found"));

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planMiembroService.asignarPlan(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el plan no existe")
    public void testAsignarPlanNoExiste() {
        // GIVEN
        PlanMiembroRequest request = new PlanMiembroRequest(1L, 99L, LocalDate.of(2026, 1, 1));
        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planMiembroService.asignarPlan(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el plan está INACTIVO")
    public void testAsignarPlanInactivo() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("INACTIVO").build();
        PlanMiembroRequest request = new PlanMiembroRequest(1L, 1L, LocalDate.of(2026, 1, 1));
        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> planMiembroService.asignarPlan(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el miembro ya tiene un plan ACTIVO")
    public void testAsignarPlanMiembroYaTienePlanActivo() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        PlanMiembroRequest request = new PlanMiembroRequest(1L, 1L, LocalDate.of(2026, 1, 1));
        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planMiembroRepository.existsByMiembroIdAndEstado(1L, "ACTIVO")).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> planMiembroService.asignarPlan(request));
    }

    @Test
    @DisplayName("Debe retornar un PlanMiembro por su ID")
    public void testObtenerPlanMiembroPorId() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        PlanMiembro planMiembro = PlanMiembro.builder()
                .id(1L).miembroId(1L).plan(plan)
                .fechaInicio(LocalDate.of(2026, 1, 1))
                .fechaFin(LocalDate.of(2026, 1, 31))
                .estado("ACTIVO").creadoEn(LocalDateTime.now()).build();
        when(planMiembroRepository.findById(1L)).thenReturn(Optional.of(planMiembro));

        // WHEN
        PlanMiembroResponse result = planMiembroService.obtenerPlanMiembroPorId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar un PlanMiembro que no existe")
    public void testObtenerPlanMiembroPorIdNoEncontrado() {
        // GIVEN
        when(planMiembroRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planMiembroService.obtenerPlanMiembroPorId(99L));
    }

    @Test
    @DisplayName("Debe retornar todos los planes de miembros")
    public void testObtenerTodosPlanMiembro() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        PlanMiembro planMiembro = PlanMiembro.builder()
                .id(1L).miembroId(1L).plan(plan)
                .fechaInicio(LocalDate.of(2026, 1, 1))
                .fechaFin(LocalDate.of(2026, 1, 31))
                .estado("ACTIVO").creadoEn(LocalDateTime.now()).build();
        when(planMiembroRepository.findAll()).thenReturn(List.of(planMiembro));

        // WHEN
        List<PlanMiembroResponse> result = planMiembroService.obtenerTodosPlanMiembro();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe retornar los planes asignados a un miembro específico")
    public void testObtenerPlanMiembroPorMiembroId() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        PlanMiembro planMiembro = PlanMiembro.builder()
                .id(1L).miembroId(1L).plan(plan)
                .fechaInicio(LocalDate.of(2026, 1, 1))
                .fechaFin(LocalDate.of(2026, 1, 31))
                .estado("ACTIVO").creadoEn(LocalDateTime.now()).build();
        when(planMiembroRepository.findByMiembroId(1L)).thenReturn(List.of(planMiembro));

        // WHEN
        List<PlanMiembroResponse> result = planMiembroService.obtenerPlanMiembroPorMiembroId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).miembroId());
    }
}