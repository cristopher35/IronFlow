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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Miembros V1", description = "Operaciones CRUD básicas sobre miembros del gimnasio")
public class MiembroController {

    private final MiembroService miembroService;

    @PostMapping
    @Operation(summary = "Crear un nuevo miembro", description = "Registra un nuevo miembro en el sistema validando que el RUT y el email sean únicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Miembro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "409", description = "RUT o email ya registrado")
    })
    public ResponseEntity<MiembroResponse> crearMiembro(@Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroService.crearMiembro(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener miembro por ID", description = "Retorna los datos de un miembro específico según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro encontrado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public ResponseEntity<MiembroResponse> obtenerMiembroPorId(@PathVariable Long id) {
        return ResponseEntity.ok(miembroService.obtenerMiembroPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los miembros", description = "Retorna la lista completa de miembros registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<MiembroResponse>> obtenerTodosLosMiembros() {
        return ResponseEntity.ok(miembroService.obtenerTodosLosMiembros());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un miembro existente", description = "Actualiza los datos de un miembro. No permite actualizar miembros con estado INACTIVO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El miembro está INACTIVO y no puede ser actualizado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado"),
            @ApiResponse(responseCode = "409", description = "RUT o email ya registrado por otro miembro")
    })
    public ResponseEntity<MiembroResponse> actualizarMiembro(@PathVariable Long id, @Valid @RequestBody MiembroRequest request) {
        return ResponseEntity.ok(miembroService.actualizarMiembro(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un miembro", description = "Elimina un miembro del sistema según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Miembro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public ResponseEntity<Void> eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
        return ResponseEntity.noContent().build();
    }

}