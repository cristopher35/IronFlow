package cl.duocuc.crmenesesn.paymentservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

import cl.duocuc.crmenesesn.paymentservice.client.MembershipClient;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;
import cl.duocuc.crmenesesn.paymentservice.model.Pago;
import cl.duocuc.crmenesesn.paymentservice.repository.PagoRepository;
import cl.duocuc.crmenesesn.paymentservice.service.PagoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
public class PagoServiceTest {

    @Autowired
    private PagoService pagoService;

    @MockitoBean
    private PagoRepository pagoRepository;

    @MockitoBean
    private MembershipClient membershipClient;

    @Test
    @DisplayName("Debe registrar un pago correctamente cuando el miembro tiene un plan activo")
    public void testRegistrarPago() {
        // GIVEN
        PagoRequest request = new PagoRequest(1L, 29990.0, "TARJETA");
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ACTIVO").build();

        when(membershipClient.getPlanMiembroByMiembroId(1L)).thenReturn(List.of(new Object()));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        PagoResponse result = pagoService.registrarPago(request);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.miembroId());
        assertEquals("ACTIVO", result.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el miembro no tiene un plan activo")
    public void testRegistrarPagoSinPlanActivo() {
        // GIVEN
        PagoRequest request = new PagoRequest(1L, 29990.0, "TARJETA");
        when(membershipClient.getPlanMiembroByMiembroId(1L)).thenReturn(Collections.emptyList());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> pagoService.registrarPago(request));
    }

    @Test
    @DisplayName("Debe lanzar excepción si membership-service no responde correctamente")
    public void testRegistrarPagoErrorComunicacion() {
        // GIVEN
        PagoRequest request = new PagoRequest(1L, 29990.0, "TARJETA");
        when(membershipClient.getPlanMiembroByMiembroId(1L)).thenThrow(new RuntimeException("Connection refused"));

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> pagoService.registrarPago(request));
    }

    @Test
    @DisplayName("Debe retornar un pago por su ID")
    public void testObtenerPagoPorId() {
        // GIVEN
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ACTIVO").build();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // WHEN
        PagoResponse result = pagoService.obtenerPagoPorId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar un pago que no existe")
    public void testObtenerPagoPorIdNoEncontrado() {
        // GIVEN
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> pagoService.obtenerPagoPorId(99L));
    }

    @Test
    @DisplayName("Debe retornar todos los pagos de un miembro")
    public void testObtenerPagosPorMiembro() {
        // GIVEN
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ACTIVO").build();
        when(pagoRepository.findByMiembroId(1L)).thenReturn(List.of(pago));

        // WHEN
        List<PagoResponse> result = pagoService.obtenerPagosPorMiembro(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).miembroId());
    }

    @Test
    @DisplayName("Debe retornar todos los pagos registrados")
    public void testObtenerTodosLosPagos() {
        // GIVEN
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ACTIVO").build();
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        // WHEN
        List<PagoResponse> result = pagoService.obtenerTodosLosPagos();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Debe anular un pago correctamente")
    public void testAnularPago() {
        // GIVEN
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ACTIVO").build();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        PagoResponse result = pagoService.anularPago(1L);

        // THEN
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al anular un pago que no existe")
    public void testAnularPagoNoEncontrado() {
        // GIVEN
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NoSuchElementException.class, () -> pagoService.anularPago(99L));
    }

    @Test
    @DisplayName("Debe lanzar excepción al anular un pago ya anulado")
    public void testAnularPagoYaAnulado() {
        // GIVEN
        Pago pago = Pago.builder()
                .id(1L).miembroId(1L).monto(29990.0)
                .fechaPago(LocalDateTime.now()).metodoPago("TARJETA")
                .estado("ANULADO").build();
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> pagoService.anularPago(1L));
    }
}