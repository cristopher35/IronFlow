package com.ironflow.notificationservice.service;

import com.ironflow.notificationservice.client.MemberClient;
import com.ironflow.notificationservice.dto.NotificacionRequest;
import com.ironflow.notificationservice.dto.NotificacionResponse;
import com.ironflow.notificationservice.exception.RecursoNoEncontradoException;
import com.ironflow.notificationservice.model.Notificacion;
import com.ironflow.notificationservice.repository.NotificacionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {
    @Mock NotificacionRepository repository;
    @Mock MemberClient memberClient;
    @InjectMocks NotificacionService service;

    @Test
    @DisplayName("Debe crear notificación normalizando estado y canal")
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
    @DisplayName("Debe rechazar creación si el miembro no existe")
    void rechazaMiembroInexistente() {
        when(memberClient.getMemberById(2L)).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> service.crearNotificacion(request(2L)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe traducir NoSuchElementException del cliente remoto a recurso no encontrado")
    void traduceNoSuchElementDelCliente() {
        when(memberClient.getMemberById(3L)).thenThrow(new NoSuchElementException("no existe"));

        assertThrows(RecursoNoEncontradoException.class, () -> service.crearNotificacion(request(3L)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe propagar indisponibilidad de member-service")
    void propagaIndisponibilidadMemberService() {
        when(memberClient.getMemberById(4L)).thenThrow(new IllegalStateException("down"));

        assertThrows(IllegalStateException.class, () -> service.crearNotificacion(request(4L)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar notificaciones")
    void listaNotificaciones() {
        when(repository.findAll()).thenReturn(List.of(notificacion(1L, 1L)));

        List<NotificacionResponse> response = service.listarNotificaciones();

        assertEquals(1, response.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Debe buscar notificación por id")
    void buscaNotificacionPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion(1L, 1L)));

        NotificacionResponse response = service.buscarPorId(1L);

        assertEquals(1L, response.id());
        assertEquals("EMAIL", response.canal());
    }

    @Test
    @DisplayName("Debe fallar al buscar notificación inexistente")
    void fallaBuscarNotificacionInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(99L));
    }

    @Test
    @DisplayName("Debe actualizar conservando fecha de envío y revalidando miembro")
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

    @Test
    @DisplayName("Debe fallar al actualizar notificación inexistente")
    void fallaActualizarNotificacionInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizarNotificacion(99L, request(1L)));
        verify(memberClient, never()).getMemberById(anyLong());
    }

    @Test
    @DisplayName("Debe listar por canal y por miembro")
    void listaPorCanalYMiembro() {
        when(repository.findByCanalIgnoreCase("EMAIL")).thenReturn(List.of(notificacion(1L, 1L)));
        when(repository.findByMiembroId(1L)).thenReturn(List.of(notificacion(1L, 1L)));

        assertEquals(1, service.listarPorCanal("EMAIL").size());
        assertEquals(1, service.listarPorMiembro(1L).size());
        verify(repository).findByCanalIgnoreCase("EMAIL");
        verify(repository).findByMiembroId(1L);
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

    private Notificacion notificacion(Long id, Long miembroId) {
        return Notificacion.builder()
                .id(id)
                .miembroId(miembroId)
                .canal("EMAIL")
                .asunto("Bienvenida")
                .mensaje("Mensaje de prueba")
                .estado("PENDIENTE")
                .fechaEnvio(LocalDateTime.now())
                .build();
    }
}
