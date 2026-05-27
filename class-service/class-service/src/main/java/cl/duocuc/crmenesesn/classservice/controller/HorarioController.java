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

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @PostMapping
    public ResponseEntity<HorarioResponse> crearHorario(@Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.crearHorario(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponse> obtenerHorarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerHorarioPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<HorarioResponse>> obtenerTodosLosHorarios() {
        return ResponseEntity.ok(horarioService.obtenerTodosLosHorarios());
    }

    @GetMapping("/class/{tipoClaseId}")
    public ResponseEntity<List<HorarioResponse>> obtenerHorariosPorTipoClase(@PathVariable Long tipoClaseId) {
        return ResponseEntity.ok(horarioService.obtenerHorariosPorTipoClase(tipoClaseId));
    }

    @GetMapping("/trainer/{entrenadorId}")
    public ResponseEntity<List<HorarioResponse>> obtenerHorariosPorEntrenador(@PathVariable Long entrenadorId) {
        return ResponseEntity.ok(horarioService.obtenerHorariosPorEntrenador(entrenadorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponse> actualizarHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(horarioService.actualizarHorario(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}