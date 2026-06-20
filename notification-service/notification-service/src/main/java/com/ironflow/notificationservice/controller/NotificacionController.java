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

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificacionResponse> crearNotificacion(@Valid @RequestBody NotificacionRequest request) {
        NotificacionResponse respuesta = notificacionService.crearNotificacion(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/notificaciones/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listarNotificaciones() {
        return ResponseEntity.ok(notificacionService.listarNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponse> actualizarNotificacion(@PathVariable Long id, @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(notificacionService.actualizarNotificacion(id, request));
    }

    @GetMapping("/canal/{canal}")
    public ResponseEntity<List<NotificacionResponse>> listarPorCanal(@PathVariable String canal) {
        return ResponseEntity.ok(notificacionService.listarPorCanal(canal));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<NotificacionResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(notificacionService.listarPorMiembro(miembroId));
    }
}
