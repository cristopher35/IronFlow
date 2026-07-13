package com.ironflow.equipmentservice.controller;

import com.ironflow.equipmentservice.dto.EquipoRequest;
import com.ironflow.equipmentservice.dto.EquipoResponse;
import com.ironflow.equipmentservice.service.EquipoService;
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

class EquipoControllerTest {

    private final EquipoService service = mock(EquipoService.class);
    private final EquipoController controller = new EquipoController(service);

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void delegatesEquipmentEndpoints() {
        // Given
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        EquipoRequest request = request();
        EquipoResponse response = new EquipoResponse(1L, "Caminadora", "Cardio", "DISPONIBLE", "Sala 1", 4, false);
        when(service.crearEquipo(request)).thenReturn(response);
        when(service.listarEquipos()).thenReturn(List.of(response));
        when(service.buscarPorId(1L)).thenReturn(response);
        when(service.actualizarEquipo(1L, request)).thenReturn(response);
        when(service.listarPorEstado("DISPONIBLE")).thenReturn(List.of(response));

        // When / Then
        assertEquals(201, controller.crearEquipo(request).getStatusCode().value());
        assertEquals(List.of(response), controller.listarEquipos().getBody());
        assertEquals(response, controller.buscarPorId(1L).getBody());
        assertEquals(response, controller.actualizarEquipo(1L, request).getBody());
        assertEquals(204, controller.eliminarEquipo(1L).getStatusCode().value());
        assertEquals(List.of(response), controller.listarPorEstado("DISPONIBLE").getBody());
        verify(service).eliminarEquipo(1L);
    }

    private EquipoRequest request() {
        EquipoRequest request = new EquipoRequest();
        request.setNombre("Caminadora");
        request.setCategoria("Cardio");
        request.setEstado("DISPONIBLE");
        request.setUbicacion("Sala 1");
        request.setCantidadDisponible(4);
        request.setMantenimientoRequerido(false);
        return request;
    }
}
