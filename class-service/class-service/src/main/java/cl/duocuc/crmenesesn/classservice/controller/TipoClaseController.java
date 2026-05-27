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

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class TipoClaseController {

    private final TipoClaseService tipoClaseService;

    @PostMapping
    public ResponseEntity<TipoClaseResponse> crearTipoClase(@Valid @RequestBody TipoClaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoClaseService.crearTipoClase(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoClaseResponse> obtenerTipoClasePorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoClaseService.obtenerTipoClasePorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoClaseResponse>> obtenerTodosLosTiposClase() {
        return ResponseEntity.ok(tipoClaseService.obtenerTodosLosTiposClase());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoClaseResponse> actualizarTipoClase(@PathVariable Long id, @Valid @RequestBody TipoClaseRequest request) {
        return ResponseEntity.ok(tipoClaseService.actualizarTipoClase(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoClase(@PathVariable Long id) {
        tipoClaseService.eliminarTipoClase(id);
        return ResponseEntity.noContent().build();
    }
}