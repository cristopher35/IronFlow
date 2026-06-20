package com.ironflow.trainerservice.controller;

import com.ironflow.trainerservice.dto.EntrenadorRequest;
import com.ironflow.trainerservice.dto.EntrenadorResponse;
import com.ironflow.trainerservice.service.EntrenadorService;
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

@RestController
@RequestMapping("/api/entrenadores")
@RequiredArgsConstructor
@Tag(name = "Entrenadores", description = "Administración y baja lógica de entrenadores")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    @PostMapping
    @Operation(summary = "Crear entrenador", description = "Registra un entrenador; retorna 201, 400 por datos inválidos o 409 por correo duplicado")
    public ResponseEntity<EntrenadorResponse> crearEntrenador(@Valid @RequestBody EntrenadorRequest request) {
        EntrenadorResponse respuesta = entrenadorService.crearEntrenador(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/entrenadores/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar entrenadores activos")
    public ResponseEntity<List<EntrenadorResponse>> listarEntrenadores() {
        return ResponseEntity.ok(entrenadorService.listarEntrenadores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrenador por ID", description = "Retorna 200 o 404")
    public ResponseEntity<EntrenadorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entrenadorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar entrenador", description = "Retorna 200, 404 o 409")
    public ResponseEntity<EntrenadorResponse> actualizarEntrenador(@PathVariable Long id, @Valid @RequestBody EntrenadorRequest request) {
        return ResponseEntity.ok(entrenadorService.actualizarEntrenador(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar entrenador", description = "Realiza baja lógica y retorna 204")
    public ResponseEntity<Void> eliminarEntrenador(@PathVariable Long id) {
        entrenadorService.eliminarEntrenador(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/especialidad/{especialidad}")
    @Operation(summary = "Listar entrenadores activos por especialidad")
    public ResponseEntity<List<EntrenadorResponse>> listarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(entrenadorService.listarPorEspecialidad(especialidad));
    }
}
