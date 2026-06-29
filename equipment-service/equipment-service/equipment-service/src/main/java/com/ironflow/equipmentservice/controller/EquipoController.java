package com.ironflow.equipmentservice.controller;

import com.ironflow.equipmentservice.dto.EquipoRequest;
import com.ironflow.equipmentservice.dto.EquipoResponse;
import com.ironflow.equipmentservice.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "Inventario, estado y mantenimiento")
public class EquipoController {

    private final EquipoService equipoService;

    @PostMapping
    @Operation(summary = "Crear equipo", description = "Retorna 201 o 400 si estado, stock y mantenimiento son inconsistentes")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Equipo creado"), @ApiResponse(responseCode = "400", description = "Datos o estado inconsistentes")})
    public ResponseEntity<EquipoResponse> crearEquipo(@Valid @RequestBody EquipoRequest request) {
        EquipoResponse respuesta = equipoService.crearEquipo(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/equipos/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar equipos")
    public ResponseEntity<List<EquipoResponse>> listarEquipos() {
        return ResponseEntity.ok(equipoService.listarEquipos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar equipo por ID", description = "Retorna 200 o 404")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Equipo encontrado"), @ApiResponse(responseCode = "404", description = "Equipo no encontrado")})
    public ResponseEntity<EquipoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar equipo", description = "Retorna 200, 400, 404 o 409")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Equipo actualizado"), @ApiResponse(responseCode = "400", description = "Datos inconsistentes"), @ApiResponse(responseCode = "404", description = "Equipo no encontrado"), @ApiResponse(responseCode = "409", description = "Equipo inactivo")})
    public ResponseEntity<EquipoResponse> actualizarEquipo(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.ok(equipoService.actualizarEquipo(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar equipo", description = "Realiza baja lógica, pone stock en cero y retorna 204")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Equipo desactivado"), @ApiResponse(responseCode = "404", description = "Equipo no encontrado")})
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar equipos por estado")
    public ResponseEntity<List<EquipoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoService.listarPorEstado(estado));
    }
}
