package cl.duocuc.crmenesesn.bookingservice.controller;

import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.service.ReservaService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservaControllerTest {

    private final ReservaService service = mock(ReservaService.class);
    private final ReservaController controller = new ReservaController(service);

    @Test
    void delegatesBookingEndpoints() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 1L);
        ReservaResponse response = new ReservaResponse(5L, 10L, 1L, LocalDateTime.now(), "ACTIVA");
        when(service.crearReserva(request)).thenReturn(response);
        when(service.obtenerReservaPorId(5L)).thenReturn(response);
        when(service.obtenerReservasPorMiembro(1L)).thenReturn(List.of(response));
        when(service.obtenerReservasPorHorario(10L)).thenReturn(List.of(response));
        when(service.obtenerTodasLasReservas()).thenReturn(List.of(response));
        when(service.cancelarReserva(5L)).thenReturn(response);

        // When / Then
        assertEquals(201, controller.crearReserva(request).getStatusCode().value());
        assertEquals(response, controller.obtenerPorId(5L).getBody());
        assertEquals(List.of(response), controller.listarPorMiembro(1L).getBody());
        assertEquals(List.of(response), controller.listarPorHorario(10L).getBody());
        assertEquals(List.of(response), controller.listarTodas().getBody());
        assertEquals(response, controller.cancelarReserva(5L).getBody());
    }
}
