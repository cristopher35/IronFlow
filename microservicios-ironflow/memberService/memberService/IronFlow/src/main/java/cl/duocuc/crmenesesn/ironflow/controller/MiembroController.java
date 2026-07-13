package cl.duocuc.crmenesesn.ironflow.controller;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Miembros", description = "Gestión de miembros del gimnasio")
public class MiembroController {

    private final MiembroService miembroService;

    @PostMapping
    @Operation(summary = "Crear miembro", description = "Registra un nuevo miembro validando que el RUT y el email sean únicos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Miembro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "RUT o email duplicado")
    })
    public ResponseEntity<MiembroResponse> crearMiembro(@Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroService.crearMiembro(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar miembro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Miembro encontrado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public ResponseEntity<MiembroResponse> obtenerMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(miembroService.obtenerMiembroPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar miembros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<MiembroResponse>> obtenerTodosLosMiembros() {
        return ResponseEntity.ok(miembroService.obtenerTodosLosMiembros());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar miembro", description = "Actualiza datos del miembro y evita modificar miembros inactivos o duplicar RUT/email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Miembro actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o miembro inactivo"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado"),
            @ApiResponse(responseCode = "409", description = "RUT o email duplicado")
    })
    public ResponseEntity<MiembroResponse> actualizarMiembro(@PathVariable Long id, @Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.ok(miembroService.actualizarMiembro(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar miembro", description = "Realiza baja lógica y retorna 204")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Miembro desactivado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public ResponseEntity<Void> eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
        return ResponseEntity.noContent().build();
    }
}
