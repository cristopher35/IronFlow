package cl.duocuc.crmenesesn.bookingservice.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    @Test
    void buildsExpectedErrorResponses() {
        // Given
        when(request.getRequestURI()).thenReturn("/api/bookings/99");

        // When / Then
        assertEquals(404, handler.handleNotFound(new EntityNotFoundException("reserva no encontrada"), request).getStatusCode().value());
        assertEquals(409, handler.handleConflict(new IllegalArgumentException("reserva duplicada"), request).getStatusCode().value());
        assertEquals(500, handler.handleGeneral(new RuntimeException("boom"), request).getStatusCode().value());
    }
}
