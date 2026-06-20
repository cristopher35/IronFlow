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

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @PostMapping
    public ResponseEntity<EquipoResponse> crearEquipo(@Valid @RequestBody EquipoRequest request) {
        EquipoResponse respuesta = equipoService.crearEquipo(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/equipos/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<EquipoResponse>> listarEquipos() {
        return ResponseEntity.ok(equipoService.listarEquipos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponse> actualizarEquipo(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
        return ResponseEntity.ok(equipoService.actualizarEquipo(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EquipoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoService.listarPorEstado(estado));
    }
}
