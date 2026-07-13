package cl.duocuc.crmenesesn.ironflow.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void buildsExpectedErrorResponses() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/members/99");

        // When / Then
        assertEquals(404, handler.handleNotFound(new NoSuchElementException("miembro no encontrado"), request).getStatusCode().value());
        assertEquals(409, handler.handleConflict(new IllegalArgumentException("rut duplicado"), request).getStatusCode().value());
        assertEquals(500, handler.handleGeneral(new RuntimeException("boom"), request).getStatusCode().value());
    }
}
