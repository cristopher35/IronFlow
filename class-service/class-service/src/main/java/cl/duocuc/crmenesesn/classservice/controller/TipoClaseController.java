package cl.duocuc.crmenesesn.classservice.controller;

import cl.duocuc.crmenesesn.classservice.dto.TipoClaseRequest;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseResponse;
import cl.duocuc.crmenesesn.classservice.service.TipoClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Tag(name = "Tipos de clase", description = "Catálogo de disciplinas ofrecidas")
public class TipoClaseController {

    private final TipoClaseService tipoClaseService;

    @PostMapping
    @Operation(summary = "Crear tipo de clase")
    public ResponseEntity<TipoClaseResponse> crearTipoClase(@Valid @RequestBody TipoClaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoClaseService.crearTipoClase(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de clase por ID")
    public ResponseEntity<TipoClaseResponse> obtenerTipoClasePorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoClaseService.obtenerTipoClasePorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar tipos de clase")
    public ResponseEntity<List<TipoClaseResponse>> obtenerTodosLosTiposClase() {
        return ResponseEntity.ok(tipoClaseService.obtenerTodosLosTiposClase());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de clase")
    public ResponseEntity<TipoClaseResponse> actualizarTipoClase(@PathVariable Long id, @Valid @RequestBody TipoClaseRequest request) {
        return ResponseEntity.ok(tipoClaseService.actualizarTipoClase(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar tipo de clase", description = "Rechaza la baja cuando existen horarios activos")
    public ResponseEntity<Void> eliminarTipoClase(@PathVariable Long id) {
        tipoClaseService.eliminarTipoClase(id);
        return ResponseEntity.noContent().build();
    }
}
