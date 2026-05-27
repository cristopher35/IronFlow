package cl.duocuc.crmenesesn.ironflow.controller;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MiembroController {

    private final MiembroService miembroService;

    @PostMapping
    public ResponseEntity<MiembroResponse> crearMiembro(@Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroService.crearMiembro(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiembroResponse> obtenerMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(miembroService.obtenerMiembroPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MiembroResponse>> obtenerTodosLosMiembros() {
        return ResponseEntity.ok(miembroService.obtenerTodosLosMiembros());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroResponse> actualizarMiembro(@PathVariable Long id, @Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.ok(miembroService.actualizarMiembro(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
        return ResponseEntity.noContent().build();
    }

}