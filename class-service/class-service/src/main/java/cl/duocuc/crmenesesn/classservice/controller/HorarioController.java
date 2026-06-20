package cl.duocuc.crmenesesn.classservice.controller;

import cl.duocuc.crmenesesn.classservice.dto.HorarioRequest;
import cl.duocuc.crmenesesn.classservice.dto.HorarioResponse;
import cl.duocuc.crmenesesn.classservice.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Horarios", description = "Programación, aforo y asignación de entrenadores")
public class HorarioController {

    private final HorarioService horarioService;

    @PostMapping
    @Operation(summary = "Crear horario", description = "Valida clase activa, entrenador remoto, fecha futura y conflictos; retorna 201, 400, 404 o 409")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Horario creado"), @ApiResponse(responseCode = "400", description = "Datos inválidos"), @ApiResponse(responseCode = "404", description = "Clase o entrenador no encontrado"), @ApiResponse(responseCode = "409", description = "Conflicto de horario"), @ApiResponse(responseCode = "500", description = "trainer-service no disponible")})
    public ResponseEntity<HorarioResponse> crearHorario(@Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.crearHorario(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar horario por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Horario encontrado"), @ApiResponse(responseCode = "404", description = "Horario no encontrado")})
    public ResponseEntity<HorarioResponse> obtenerHorarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerHorarioPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar horarios")
    public ResponseEntity<List<HorarioResponse>> obtenerTodosLosHorarios() {
        return ResponseEntity.ok(horarioService.obtenerTodosLosHorarios());
    }

    @GetMapping("/class/{tipoClaseId}")
    @Operation(summary = "Listar horarios por tipo de clase")
    public ResponseEntity<List<HorarioResponse>> obtenerHorariosPorTipoClase(@PathVariable Long tipoClaseId) {
        return ResponseEntity.ok(horarioService.obtenerHorariosPorTipoClase(tipoClaseId));
    }

    @GetMapping("/trainer/{entrenadorId}")
    @Operation(summary = "Listar horarios por entrenador")
    public ResponseEntity<List<HorarioResponse>> obtenerHorariosPorEntrenador(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(horarioService.obtenerHorariosPorEntrenador(entrenadorId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario", description = "Controla aforo actual, clase activa y conflictos del entrenador")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Horario actualizado"), @ApiResponse(responseCode = "400", description = "Datos inválidos"), @ApiResponse(responseCode = "404", description = "Recurso no encontrado"), @ApiResponse(responseCode = "409", description = "Conflicto de negocio")})
    public ResponseEntity<HorarioResponse> actualizarHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(horarioService.actualizarHorario(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar horario", description = "Realiza baja lógica y retorna 204")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Horario desactivado"), @ApiResponse(responseCode = "404", description = "Horario no encontrado")})
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
