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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Miembros", description = "Gestión de miembros del gimnasio")
public class MiembroController {

    private final MiembroService miembroService;

    @PostMapping
    @Operation(summary = "Crear miembro", description = "Retorna 201, 400 por datos inválidos o 409 por RUT/email duplicado")
    public ResponseEntity<MiembroResponse> crearMiembro(@Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroService.crearMiembro(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar miembro por ID")
    public ResponseEntity<MiembroResponse> obtenerMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(miembroService.obtenerMiembroPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar miembros")
    public ResponseEntity<List<MiembroResponse>> obtenerTodosLosMiembros() {
        return ResponseEntity.ok(miembroService.obtenerTodosLosMiembros());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar miembro")
    public ResponseEntity<MiembroResponse> actualizarMiembro(@PathVariable Long id, @Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.ok(miembroService.actualizarMiembro(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar miembro", description = "Realiza baja lógica y retorna 204")
    public ResponseEntity<Void> eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
        return ResponseEntity.noContent().build();
    }

}
