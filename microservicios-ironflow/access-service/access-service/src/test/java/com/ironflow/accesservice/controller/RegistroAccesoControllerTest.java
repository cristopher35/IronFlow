package com.ironflow.accesservice.controller;

import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.service.RegistroAccesoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistroAccesoControllerTest {

    private final RegistroAccesoService service = mock(RegistroAccesoService.class);
    private final RegistroAccesoController controller = new RegistroAccesoController(service);

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void resetRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void delegatesAccessEndpoints() {
        RegistroAccesoRequest request = new RegistroAccesoRequest();
        request.setMiembroId(1L);
        request.setObservacion("Ingreso");
        RegistroAccesoResponse response = new RegistroAccesoResponse(1L, 1L, "PERMITIDO", LocalDateTime.now(), "Ingreso");
        when(service.verificarAcceso(request)).thenReturn(response);
        when(service.listarRegistros()).thenReturn(List.of(response));
        when(service.buscarPorId(1L)).thenReturn(response);
        when(service.listarPorMiembro(1L)).thenReturn(List.of(response));

        assertEquals(201, controller.verificarAcceso(request).getStatusCode().value());
        assertEquals(1, controller.listarRegistros().getBody().size());
        assertEquals(response, controller.buscarPorId(1L).getBody());
        assertEquals(1, controller.listarPorMiembro(1L).getBody().size());
    }
}
