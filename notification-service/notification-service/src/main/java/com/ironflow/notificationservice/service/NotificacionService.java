package com.ironflow.notificationservice.service;

import com.ironflow.notificationservice.client.MemberClient;
import com.ironflow.notificationservice.dto.NotificacionRequest;
import com.ironflow.notificationservice.dto.NotificacionResponse;
import com.ironflow.notificationservice.exception.RecursoNoEncontradoException;
import com.ironflow.notificationservice.model.Notificacion;
import com.ironflow.notificationservice.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final MemberClient memberClient;

    @Transactional
    public NotificacionResponse crearNotificacion(NotificacionRequest request) {
        log.info("Registrando notificacion para miembro ID: {}", request.getMiembroId());

        // Validar que el miembro existe en member-service
        try {
            memberClient.getMemberById(request.getMiembroId());
        } catch (Exception e) {
            log.warn("Miembro no encontrado con id: {}", request.getMiembroId());
            throw new RecursoNoEncontradoException("No existe un miembro con id: " + request.getMiembroId());
        }

        Notificacion notificacion = Notificacion.builder()
                .miembroId(request.getMiembroId())
                .canal(request.getCanal())
                .asunto(request.getAsunto())
                .mensaje(request.getMensaje())
                .estado(request.getEstado())
                .fechaEnvio(LocalDateTime.now())
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificacion registrada con ID: {}", guardada.getId());
        return mapearRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> listarNotificaciones() {
        return notificacionRepository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificacionResponse buscarPorId(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una notificacion con id " + id));

        return mapearRespuesta(notificacion);
    }

    @Transactional
    public NotificacionResponse actualizarNotificacion(Long id, NotificacionRequest request) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una notificacion con id " + id));

        notificacion.setMiembroId(request.getMiembroId());
        notificacion.setCanal(request.getCanal());
        notificacion.setAsunto(request.getAsunto());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setEstado(request.getEstado());

        Notificacion actualizada = notificacionRepository.save(notificacion);
        log.info("Notificacion actualizada con ID: {}", actualizada.getId());

        return mapearRespuesta(actualizada);
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> listarPorCanal(String canal) {
        return notificacionRepository.findByCanalIgnoreCase(canal)
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    private NotificacionResponse mapearRespuesta(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getMiembroId(),
                notificacion.getCanal(),
                notificacion.getAsunto(),
                notificacion.getMensaje(),
                notificacion.getEstado(),
                notificacion.getFechaEnvio()
        );
    }
}