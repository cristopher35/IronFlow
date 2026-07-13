package com.ironflow.branchservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManejadorGlobalExcepcionesTest {

    private final ManejadorGlobalExcepciones handler = new ManejadorGlobalExcepciones();
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void buildsExpectedErrorResponses() {
        when(request.getRequestURI()).thenReturn("/api/sucursales/99");

        assertEquals(404, handler.manejarNoEncontrado(new RecursoNoEncontradoException("no existe"), request).getStatusCode().value());
        assertEquals(400, handler.manejarSolicitudIncorrecta(new IllegalArgumentException("malo"), request).getStatusCode().value());
        assertEquals(500, handler.manejarInesperado(new RuntimeException("boom"), request).getStatusCode().value());
    }
}
