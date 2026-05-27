package cl.duocuc.crmenesesn.membershipservice.controller;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;
import cl.duocuc.crmenesesn.membershipservice.service.PlanMiembroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/planes-miembros")
@RequiredArgsConstructor
public class PlanMiembroController {

    private final PlanMiembroService planMiembroService;

    @PostMapping
    public ResponseEntity<PlanMiembroResponse> asignarPlan(@Valid @RequestBody PlanMiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planMiembroService.asignarPlan(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanMiembroResponse> obtenerPlanMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planMiembroService.obtenerPlanMiembroPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanMiembroResponse>> obtenerTodosPlanMiembro() {
        return ResponseEntity.ok(planMiembroService.obtenerTodosPlanMiembro());
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<PlanMiembroResponse>> obtenerPlanMiembroPorMiembroId(@PathVariable Long miembroId) {
        return ResponseEntity.ok(planMiembroService.obtenerPlanMiembroPorMiembroId(miembroId));
    }

}