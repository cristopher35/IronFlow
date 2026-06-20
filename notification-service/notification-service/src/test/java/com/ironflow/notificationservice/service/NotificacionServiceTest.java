package com.ironflow.notificationservice.service;

import com.ironflow.notificationservice.client.MemberClient;
import com.ironflow.notificationservice.dto.NotificacionRequest;
import com.ironflow.notificationservice.dto.NotificacionResponse;
import com.ironflow.notificationservice.exception.RecursoNoEncontradoException;
import com.ironflow.notificationservice.model.Notificacion;
import com.ironflow.notificationservice.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {
    @Mock NotificacionRepository repository;
    @Mock MemberClient memberClient;
    @InjectMocks NotificacionService service;

    @Test
    void creaNotificacionYNormalizaEstadoYCanal() {
        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(repository.save(any())).thenAnswer(invocation -> {
            Notificacion n = invocation.getArgument(0);
            n.setId(1L);
            return n;
        });

        NotificacionResponse response = service.crearNotificacion(request(1L));

        assertEquals("EMAIL", response.canal());
        assertEquals("PENDIENTE", response.estado());
        assertNotNull(response.fechaEnvio());
    }

    @Test
    void rechazaMiembroInexistente() {
        when(memberClient.getMemberById(2L)).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> service.crearNotificacion(request(2L)));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarConservaFechaDeEnvioYRevalidaMiembro() {
        LocalDateTime fecha = LocalDateTime.now().minusDays(1);
        Notificacion actual = Notificacion.builder().id(1L).miembroId(1L).canal("SMS")
                .asunto("Anterior").mensaje("Anterior").estado("PENDIENTE").fechaEnvio(fecha).build();
        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(memberClient.getMemberById(1L)).thenReturn(new Object());
        when(repository.save(actual)).thenReturn(actual);

        NotificacionResponse response = service.actualizarNotificacion(1L, request(1L));

        assertEquals(fecha, response.fechaEnvio());
        verify(memberClient).getMemberById(1L);
    }

    private NotificacionRequest request(Long miembroId) {
        NotificacionRequest request = new NotificacionRequest();
        request.setMiembroId(miembroId);
        request.setCanal("email");
        request.setAsunto("Bienvenida");
        request.setMensaje("Mensaje de prueba");
        request.setEstado("pendiente");
        return request;
    }
}
