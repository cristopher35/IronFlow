package com.ironflow.accesservice.controller;

import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.service.RegistroAccesoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accesos")
@RequiredArgsConstructor
@Tag(name = "Accesos", description = "Control de acceso y validación de membresías")
public class RegistroAccesoController {

    private final RegistroAccesoService registroAccesoService;

    @PostMapping("/verificar")
    @Operation(summary = "Verificar acceso", description = "Valida si un miembro puede ingresar según su membresía activa")
    public ResponseEntity<RegistroAccesoResponse> verificarAcceso(@Valid @RequestBody RegistroAccesoRequest request) {
        RegistroAccesoResponse respuesta = registroAccesoService.verificarAcceso(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/accesos/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar registros de acceso")
    public ResponseEntity<List<RegistroAccesoResponse>> listarRegistros() {
        return ResponseEntity.ok(registroAccesoService.listarRegistros());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro de acceso por ID")
    public ResponseEntity<RegistroAccesoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(registroAccesoService.buscarPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    @Operation(summary = "Listar accesos por miembro")
    public ResponseEntity<List<RegistroAccesoResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(registroAccesoService.listarPorMiembro(miembroId));
    }
}
