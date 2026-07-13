package cl.duocuc.crmenesesn.membershipservice.service;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.model.Plan;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Override
    public PlanResponse crearPlan(PlanRequest request) {
        log.info("Creando plan con nombre: {}", request.nombre());
        if (planRepository.existsByNombre(request.nombre())) {
            log.warn("Ya existe un plan con nombre: {}", request.nombre());
            throw new IllegalArgumentException("Ya existe un plan con el nombre: " + request.nombre());
        }
        Plan plan = Plan.builder()
                .nombre(request.nombre())
                .precio(request.precio())
                .diasDuracion(request.diasDuracion())
                .build();
        Plan saved = planRepository.save(plan);
        log.info("Plan creado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public PlanResponse obtenerPlanPorId(Long id) {
        log.info("Buscando plan con id: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Plan no encontrado con id: {}", id);
                    return new NoSuchElementException("Plan no encontrado con id: " + id);
                });
        return toResponse(plan);
    }

    @Override
    public List<PlanResponse> obtenerTodosLosPlanes() {
        log.info("Obteniendo todos los planes");
        return planRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PlanResponse actualizarPlan(Long id, PlanRequest request) {
        log.info("Actualizando plan con id: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Plan no encontrado con id: {}", id);
                    return new NoSuchElementException("Plan no encontrado con id: " + id);
                });
        if (plan.getEstado().equals("INACTIVO")) {
            log.warn("Intento de actualizar plan INACTIVO con id: {}", id);
            throw new IllegalArgumentException("No se puede actualizar un plan INACTIVO");
        }
        plan.setNombre(request.nombre());
        plan.setPrecio(request.precio());
        plan.setDiasDuracion(request.diasDuracion());
        Plan saved = planRepository.save(plan);
        log.info("Plan actualizado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void eliminarPlan(Long id) {
        log.info("Desactivando plan con id: {}", id);
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Plan no encontrado con id: {}", id);
                    return new NoSuchElementException("Plan no encontrado con id: " + id);
                });
        plan.setEstado("INACTIVO");
        planRepository.save(plan);
        log.info("Plan desactivado con id: {}", id);
    }

    private PlanResponse toResponse(Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getNombre(),
                plan.getPrecio(),
                plan.getDiasDuracion(),
                plan.getEstado()
        );
    }
}