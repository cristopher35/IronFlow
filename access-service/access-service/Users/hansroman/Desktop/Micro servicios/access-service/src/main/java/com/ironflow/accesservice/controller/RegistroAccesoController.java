package com.ironflow.accesservice.controller;

import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.service.RegistroAccesoService;
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
public class RegistroAccesoController {

    private final RegistroAccesoService registroAccesoService;

    @PostMapping("/verificar")
    public ResponseEntity<RegistroAccesoResponse> verificarAcceso(@Valid @RequestBody RegistroAccesoRequest request) {
        RegistroAccesoResponse respuesta = registroAccesoService.verificarAcceso(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/accesos/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<RegistroAccesoResponse>> listarRegistros() {
        return ResponseEntity.ok(registroAccesoService.listarRegistros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAccesoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(registroAccesoService.buscarPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<RegistroAccesoResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(registroAccesoService.listarPorMiembro(miembroId));
    }
}
