package cl.duocuc.crmenesesn.membershipservice.controller;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanMiembroResponse;
import cl.duocuc.crmenesesn.membershipservice.service.PlanMiembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Planes de Miembros", description = "Asignación de planes de membresía a miembros, incluyendo comunicación con member-service")
public class PlanMiembroController {

    private final PlanMiembroService planMiembroService;

    @PostMapping
    @Operation(summary = "Asignar un plan a un miembro", description = "Asigna un plan de membresía a un miembro existente, validando contra member-service que el miembro exista y esté ACTIVO, que el plan exista y esté ACTIVO, y que el miembro no tenga ya un plan activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan asignado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El miembro o el plan están INACTIVOS, o el miembro ya tiene un plan activo"),
            @ApiResponse(responseCode = "404", description = "Miembro o plan no encontrado")
    })
    public ResponseEntity<PlanMiembroResponse> asignarPlan(@Valid @RequestBody PlanMiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planMiembroService.asignarPlan(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una asignación de plan por ID", description = "Retorna los datos de una asignación de plan-miembro específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignación encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    public ResponseEntity<PlanMiembroResponse> obtenerPlanMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planMiembroService.obtenerPlanMiembroPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las asignaciones de planes", description = "Retorna la lista completa de asignaciones de planes a miembros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<PlanMiembroResponse>> obtenerTodosPlanMiembro() {
        return ResponseEntity.ok(planMiembroService.obtenerTodosPlanMiembro());
    }

    @GetMapping("/miembro/{miembroId}")
    @Operation(summary = "Obtener planes asignados a un miembro", description = "Retorna todas las asignaciones de planes asociadas a un miembro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<PlanMiembroResponse>> obtenerPlanMiembroPorMiembroId(@PathVariable Long miembroId) {
        return ResponseEntity.ok(planMiembroService.obtenerPlanMiembroPorMiembroId(miembroId));
    }

}