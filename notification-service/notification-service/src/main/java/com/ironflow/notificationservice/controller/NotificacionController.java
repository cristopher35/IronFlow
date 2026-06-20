package com.ironflow.notificationservice.controller;

import com.ironflow.notificationservice.dto.NotificacionRequest;
import com.ironflow.notificationservice.dto.NotificacionResponse;
import com.ironflow.notificationservice.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Registro y consulta de notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    @Operation(summary = "Crear notificación", description = "Valida el miembro remoto y retorna 201, 400 o 404")
    public ResponseEntity<NotificacionResponse> crearNotificacion(@Valid @RequestBody NotificacionRequest request) {
        NotificacionResponse respuesta = notificacionService.crearNotificacion(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/notificaciones/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones")
    public ResponseEntity<List<NotificacionResponse>> listarNotificaciones() {
        return ResponseEntity.ok(notificacionService.listarNotificaciones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificación por ID", description = "Retorna 200 o 404")
    public ResponseEntity<NotificacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar notificación")
    public ResponseEntity<NotificacionResponse> actualizarNotificacion(@PathVariable Long id, @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(notificacionService.actualizarNotificacion(id, request));
    }

    @GetMapping("/canal/{canal}")
    @Operation(summary = "Listar notificaciones por canal")
    public ResponseEntity<List<NotificacionResponse>> listarPorCanal(@PathVariable String canal) {
        return ResponseEntity.ok(notificacionService.listarPorCanal(canal));
    }

    @GetMapping("/miembro/{miembroId}")
    @Operation(summary = "Listar notificaciones por miembro")
    public ResponseEntity<List<NotificacionResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(notificacionService.listarPorMiembro(miembroId));
    }
}
