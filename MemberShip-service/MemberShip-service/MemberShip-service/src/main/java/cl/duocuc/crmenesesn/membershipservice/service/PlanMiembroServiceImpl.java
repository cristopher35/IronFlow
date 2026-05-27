package cl.duocuc.crmenesesn.membershipservice.service;

import cl.duocuc.crmenesesn.membershipservice.client.MemberClient;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.model.Plan;
import cl.duocuc.crmenesesn.membershipservice.model.PlanMiembro;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanMiembroRepository;
import cl.duocuc.crmenesesn.membershipservice.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanMiembroServiceImpl implements PlanMiembroService {

    private final PlanMiembroRepository planMiembroRepository;
    private final PlanRepository planRepository;
    private final MemberClient memberClient;

    @Override
    public PlanMiembroResponse asignarPlan(PlanMiembroRequest request) {
        log.info("Asignando plan {} a miembro {}", request.planId(), request.miembroId());

        try {
            memberClient.getMemberById(request.miembroId());
        } catch (Exception e) {
            log.warn("Miembro no encontrado con id: {}", request.miembroId());
            throw new NoSuchElementException("No existe un miembro con id: " + request.miembroId());
        }

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> {
                    log.warn("Plan no encontrado con id: {}", request.planId());
                    return new NoSuchElementException("Plan no encontrado con id: " + request.planId());
                });

        if (plan.getEstado().equals("INACTIVO")) {
            log.warn("Intento de asignar plan INACTIVO con id: {}", request.planId());
            throw new IllegalArgumentException("No se puede asignar un plan INACTIVO");
        }

        if (planMiembroRepository.existsByMiembroIdAndEstado(request.miembroId(), "ACTIVO")) {
            log.warn("Miembro {} ya tiene un plan ACTIVO", request.miembroId());
            throw new IllegalArgumentException("El miembro ya tiene un plan ACTIVO asignado");
        }

        PlanMiembro planMiembro = PlanMiembro.builder()
                .miembroId(request.miembroId())
                .plan(plan)
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaInicio().plusDays(plan.getDiasDuracion()))
                .build();

        PlanMiembro saved = planMiembroRepository.save(planMiembro);
        log.info("Plan asignado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public PlanMiembroResponse obtenerPlanMiembroPorId(Long id) {
        log.info("Buscando PlanMiembro con id: {}", id);
        PlanMiembro planMiembro = planMiembroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("PlanMiembro no encontrado con id: {}", id);
                    return new NoSuchElementException("PlanMiembro no encontrado con id: " + id);
                });
        return toResponse(planMiembro);
    }

    @Override
    public List<PlanMiembroResponse> obtenerTodosPlanMiembro() {
        log.info("Obteniendo todos los planes de miembros");
        return planMiembroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PlanMiembroResponse> obtenerPlanMiembroPorMiembroId(Long miembroId) {
        log.info("Obteniendo planes del miembro con id: {}", miembroId);
        return planMiembroRepository.findByMiembroId(miembroId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PlanMiembroResponse toResponse(PlanMiembro planMiembro) {
        Plan plan = planMiembro.getPlan();
        PlanResponse planResponse = new PlanResponse(
                plan.getId(),
                plan.getNombre(),
                plan.getPrecio(),
                plan.getDiasDuracion(),
                plan.getEstado()
        );
        return new PlanMiembroResponse(
                planMiembro.getId(),
                planMiembro.getMiembroId(),
                planResponse,
                planMiembro.getFechaInicio(),
                planMiembro.getFechaFin(),
                planMiembro.getEstado(),
                planMiembro.getCreadoEn()
        );
    }
}