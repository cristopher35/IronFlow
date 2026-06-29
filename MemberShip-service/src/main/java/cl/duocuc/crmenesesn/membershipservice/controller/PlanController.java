package cl.duocuc.crmenesesn.membershipservice.controller;

import cl.duocuc.crmenesesn.membershipservice.dto.PlanRequest;
import cl.duocuc.crmenesesn.membershipservice.dto.PlanResponse;
import cl.duocuc.crmenesesn.membershipservice.service.PlanService;
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
@RequestMapping("/api/planes")
@RequiredArgsConstructor
@Tag(name = "Planes", description = "Operaciones CRUD sobre los planes de membresía del gimnasio")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(summary = "Crear un nuevo plan", description = "Registra un nuevo plan de membresía validando que el nombre sea único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "409", description = "Ya existe un plan con ese nombre")
    })
    public ResponseEntity<PlanResponse> crearPlan(@Valid @RequestBody PlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.crearPlan(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener plan por ID", description = "Retorna los datos de un plan de membresía específico según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan encontrado"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<PlanResponse> obtenerPlanPorId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.obtenerPlanPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los planes", description = "Retorna la lista completa de planes de membresía registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<PlanResponse>> obtenerTodosLosPlanes() {
        return ResponseEntity.ok(planService.obtenerTodosLosPlanes());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un plan existente", description = "Actualiza los datos de un plan de membresía existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe otro plan con ese nombre")
    })
    public ResponseEntity<PlanResponse> actualizarPlan(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(planService.actualizarPlan(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un plan", description = "Elimina un plan de membresía del sistema según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado")
    })
    public ResponseEntity<Void> eliminarPlan(@PathVariable Long id) {
        planService.eliminarPlan(id);
        return ResponseEntity.noContent().build();
    }

}