package com.ironflow.trainerservice.controller;

import com.ironflow.trainerservice.dto.EntrenadorRequest;
import com.ironflow.trainerservice.dto.EntrenadorResponse;
import com.ironflow.trainerservice.service.EntrenadorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EntrenadorControllerTest {

    private final EntrenadorService service = mock(EntrenadorService.class);
    private final EntrenadorController controller = new EntrenadorController(service);

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void delegatesTrainerEndpoints() {
        // Given
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        EntrenadorRequest request = request();
        EntrenadorResponse response = new EntrenadorResponse(1L, "Ana", "Funcional", 5, "912345678", "ana@ironflow.cl", "ACTIVO", true);
        when(service.crearEntrenador(request)).thenReturn(response);
        when(service.listarEntrenadores()).thenReturn(List.of(response));
        when(service.buscarPorId(1L)).thenReturn(response);
        when(service.actualizarEntrenador(1L, request)).thenReturn(response);
        when(service.listarPorEspecialidad("Funcional")).thenReturn(List.of(response));

        // When / Then
        assertEquals(201, controller.crearEntrenador(request).getStatusCode().value());
        assertEquals(List.of(response), controller.listarEntrenadores().getBody());
        assertEquals(response, controller.buscarPorId(1L).getBody());
        assertEquals(response, controller.actualizarEntrenador(1L, request).getBody());
        assertEquals(204, controller.eliminarEntrenador(1L).getStatusCode().value());
        assertEquals(List.of(response), controller.listarPorEspecialidad("Funcional").getBody());
        verify(service).eliminarEntrenador(1L);
    }

    private EntrenadorRequest request() {
        EntrenadorRequest request = new EntrenadorRequest();
        request.setNombre("Ana");
        request.setEspecialidad("Funcional");
        request.setAniosExperiencia(5);
        request.setTelefono("912345678");
        request.setCorreo("ana@ironflow.cl");
        request.setEstado("ACTIVO");
        return request;
    }
}
