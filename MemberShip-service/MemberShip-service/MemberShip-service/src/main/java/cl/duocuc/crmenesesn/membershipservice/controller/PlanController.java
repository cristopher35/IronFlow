package cl.duocuc.crmenesesn.membershipservice.controller;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponse> crearPlan(@Valid @RequestBody PlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.crearPlan(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> obtenerPlanPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.obtenerPlanPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> obtenerTodosLosPlanes() {
        return ResponseEntity.ok(planService.obtenerTodosLosPlanes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> actualizarPlan(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(planService.actualizarPlan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlan(@PathVariable Long id) {
        planService.eliminarPlan(id);
        return ResponseEntity.noContent().build();
    }

}