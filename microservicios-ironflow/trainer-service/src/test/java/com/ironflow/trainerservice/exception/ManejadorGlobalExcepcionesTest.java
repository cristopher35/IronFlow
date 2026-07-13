package com.ironflow.trainerservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManejadorGlobalExcepcionesTest {

    private final ManejadorGlobalExcepciones handler = new ManejadorGlobalExcepciones();
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void buildsExpectedErrorResponses() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/entrenadores/99");

        // When / Then
        assertEquals(404, handler.manejarNoEncontrado(new RecursoNoEncontradoException("no existe"), request).getStatusCode().value());
        assertEquals(409, handler.manejarConflicto(new IllegalArgumentException("duplicado"), request).getStatusCode().value());
        assertEquals(400, handler.manejarSolicitudIncorrecta(new HttpMessageNotReadableException("json invalido", null), request).getStatusCode().value());
        assertEquals(500, handler.manejarInesperado(new RuntimeException("boom"), request).getStatusCode().value());
    }
}
