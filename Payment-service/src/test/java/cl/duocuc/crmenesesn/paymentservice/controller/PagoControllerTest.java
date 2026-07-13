package cl.duocuc.crmenesesn.paymentservice.controller;

import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;
import cl.duocuc.crmenesesn.paymentservice.service.PagoService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PagoControllerTest {

    private final PagoService service = mock(PagoService.class);
    private final PagoController controller = new PagoController(service);

    @Test
    void delegatesPaymentEndpoints() {
        PagoRequest request = new PagoRequest(1L, 29990.0, "TARJETA");
        PagoResponse response = new PagoResponse(1L, 1L, 29990.0, LocalDateTime.now(), "TARJETA", "ACTIVO");
        when(service.registrarPago(request)).thenReturn(response);
        when(service.obtenerPagoPorId(1L)).thenReturn(response);
        when(service.obtenerPagosPorMiembro(1L)).thenReturn(List.of(response));
        when(service.obtenerTodosLosPagos()).thenReturn(List.of(response));
        when(service.anularPago(1L)).thenReturn(response);

        assertEquals(201, controller.registrarPago(request).getStatusCode().value());
        assertEquals(response, controller.obtenerPagoPorId(1L).getBody());
        assertEquals(1, controller.obtenerPagosPorMiembro(1L).getBody().size());
        assertEquals(1, controller.obtenerTodosLosPagos().getBody().size());
        assertEquals(response, controller.anularPago(1L).getBody());
    }
}
