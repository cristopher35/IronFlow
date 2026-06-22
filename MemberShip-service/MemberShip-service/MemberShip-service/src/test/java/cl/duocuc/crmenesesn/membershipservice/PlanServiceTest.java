package cl.duocuc.crmenesesn.membershipservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.model.Plan;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanRepository;
import cl.duocuc.crmenesesn.membershipservice.service.PlanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
public class PlanServiceTest {

    @Autowired
    private PlanService planService;

    @MockitoBean
    private PlanRepository planRepository;

    @Test
    @DisplayName("Debe retornar todos los planes correctamente")
    public void testObtenerTodosLosPlanes() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        when(planRepository.findAll()).thenReturn(List.of(plan));

        // WHEN
        List<PlanResponse> result = planService.obtenerTodosLosPlanes();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Plan Premium", result.get(0).nombre());
    }

    @Test
    @DisplayName("Debe retornar un plan por su ID")
    public void testObtenerPlanPorId() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // WHEN
        PlanResponse result = planService.obtenerPlanPorId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Plan Premium", result.nombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar un plan que no existe")
    public void testObtenerPlanPorIdNoEncontrado() {
        // GIVEN
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planService.obtenerPlanPorId(99L));
    }

    @Test
    @DisplayName("Debe crear un plan cuando los datos son válidos")
    public void testCrearPlan() {
        // GIVEN
        PlanRequest request = new PlanRequest("Plan Premium", 29990.0, 30);
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        when(planRepository.existsByNombre("Plan Premium")).thenReturn(false);
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // WHEN
        PlanResponse result = planService.crearPlan(request);

        // THEN
        assertNotNull(result);
        assertEquals("Plan Premium", result.nombre());
        assertEquals("ACTIVO", result.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre del plan ya existe")
    public void testCrearPlanNombreDuplicado() {
        // GIVEN
        PlanRequest request = new PlanRequest("Plan Premium", 29990.0, 30);
        when(planRepository.existsByNombre("Plan Premium")).thenReturn(true);

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> planService.crearPlan(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un plan que no existe")
    public void testActualizarPlanNoEncontrado() {
        // GIVEN
        PlanRequest request = new PlanRequest("Plan Premium", 29990.0, 30);
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planService.actualizarPlan(99L, request));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un plan INACTIVO")
    public void testActualizarPlanInactivo() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("INACTIVO").build();
        PlanRequest request = new PlanRequest("Plan Premium", 29990.0, 30);
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> planService.actualizarPlan(1L, request));
    }

    @Test
    @DisplayName("Debe desactivar un plan al eliminarlo")
    public void testEliminarPlan() {
        // GIVEN
        Plan plan = Plan.builder()
                .id(1L).nombre("Plan Premium").precio(29990.0)
                .diasDuracion(30).estado("ACTIVO").build();
        when(planRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        // WHEN
        planService.eliminarPlan(1L);

        // THEN
        verify(planRepository, times(1)).save(any(Plan.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar un plan que no existe")
    public void testEliminarPlanNoEncontrado() {
        // GIVEN
        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> planService.eliminarPlan(99L));
    }
}