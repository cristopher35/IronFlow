package com.ironflow.branchservice.controller;

import com.ironflow.branchservice.dto.SucursalRequest;
import com.ironflow.branchservice.dto.SucursalResponse;
import com.ironflow.branchservice.service.SucursalService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SucursalControllerTest {

    private final SucursalService service = mock(SucursalService.class);
    private final SucursalController controller = new SucursalController(service);

    @BeforeEach
    void setUpRequestContext() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void resetRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void delegatesSucursalEndpoints() {
        SucursalRequest request = request();
        SucursalResponse response = response();
        when(service.crearSucursal(request)).thenReturn(response);
        when(service.listarSucursales()).thenReturn(List.of(response));
        when(service.buscarPorId(1L)).thenReturn(response);
        when(service.actualizarSucursal(1L, request)).thenReturn(response);
        when(service.listarPorCiudad("Santiago")).thenReturn(List.of(response));

        assertEquals(201, controller.crearSucursal(request).getStatusCode().value());
        assertEquals(1, controller.listarSucursales().getBody().size());
        assertEquals(response, controller.buscarPorId(1L).getBody());
        assertEquals(response, controller.actualizarSucursal(1L, request).getBody());
        assertEquals(1, controller.listarPorCiudad("Santiago").getBody().size());
    }

    private SucursalRequest request() {
        SucursalRequest request = new SucursalRequest();
        request.setNombre("Centro");
        request.setCiudad("Santiago");
        request.setDireccion("Av. Principal 123");
        request.setTelefono("229998888");
        request.setHorarioAtencion("06:00-23:00");
        request.setEstado("ACTIVA");
        return request;
    }

    private SucursalResponse response() {
        return new SucursalResponse(1L, "Centro", "Santiago", "Av. Principal 123",
                "229998888", "06:00-23:00", "ACTIVA", true);
    }
}
