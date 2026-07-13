package cl.duocuc.crmenesesn.classservice.exception;

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
    void handleNotFoundBuilds404Body() {
        when(request.getRequestURI()).thenReturn("/api/classes/99");

        var response = handler.handleNotFound(new NoSuchElementException("no existe"), request);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("/api/classes/99", response.getBody().path());
    }

    @Test
    void handleConflictBuilds409Body() {
        when(request.getRequestURI()).thenReturn("/api/schedules");

        var response = handler.handleConflict(new IllegalArgumentException("conflicto"), request);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Conflict", response.getBody().error());
    }

    @Test
    void handleGeneralBuilds500Body() {
        when(request.getRequestURI()).thenReturn("/api/schedules");

        var response = handler.handleGeneral(new RuntimeException("boom"), request);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal Server Error", response.getBody().error());
    }
}
