package com.ironflow.notificationservice.controller;

import com.ironflow.notificationservice.dto.NotificacionRequest;
import com.ironflow.notificationservice.dto.NotificacionResponse;
import com.ironflow.notificationservice.service.NotificacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificacionControllerTest {

    private final NotificacionService service = mock(NotificacionService.class);
    private final NotificacionController controller = new NotificacionController(service);

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void delegatesNotificationEndpoints() {
        // Given
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        NotificacionRequest request = request();
        NotificacionResponse response = new NotificacionResponse(1L, 7L, "EMAIL", "Alta", "Bienvenido", "PENDIENTE", LocalDateTime.now());
        when(service.crearNotificacion(request)).thenReturn(response);
        when(service.listarNotificaciones()).thenReturn(List.of(response));
        when(service.buscarPorId(1L)).thenReturn(response);
        when(service.actualizarNotificacion(1L, request)).thenReturn(response);
        when(service.listarPorCanal("EMAIL")).thenReturn(List.of(response));
        when(service.listarPorMiembro(7L)).thenReturn(List.of(response));

        // When / Then
        assertEquals(201, controller.crearNotificacion(request).getStatusCode().value());
        assertEquals(List.of(response), controller.listarNotificaciones().getBody());
        assertEquals(response, controller.buscarPorId(1L).getBody());
        assertEquals(response, controller.actualizarNotificacion(1L, request).getBody());
        assertEquals(List.of(response), controller.listarPorCanal("EMAIL").getBody());
        assertEquals(List.of(response), controller.listarPorMiembro(7L).getBody());
    }

    private NotificacionRequest request() {
        NotificacionRequest request = new NotificacionRequest();
        request.setMiembroId(7L);
        request.setCanal("EMAIL");
        request.setAsunto("Alta");
        request.setMensaje("Bienvenido");
        request.setEstado("PENDIENTE");
        return request;
    }
}
