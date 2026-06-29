package cl.duocuc.crmenesesn.bookingservice;

import cl.duocuc.crmenesesn.bookingservice.client.ClassClient;
import cl.duocuc.crmenesesn.bookingservice.client.MemberClient;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.model.Reserva;
import cl.duocuc.crmenesesn.bookingservice.repository.ReservaRepository;
import cl.duocuc.crmenesesn.bookingservice.service.ReservaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClassClient classClient;

    @Mock
    private MemberClient memberClient;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    @DisplayName("Debe crear reserva activa cuando miembro y horario existen")
    void crearReservaHappyPath() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 1L);
        when(memberClient.getMemberById(1L)).thenReturn(Map.of("id", 1L));
        when(classClient.getHorarioById(10L)).thenReturn(Map.of("id", 10L));
        when(reservaRepository.existsByHorarioIdAndMiembroIdAndEstado(10L, 1L, "ACTIVA")).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> reservaGuardada(invocation.getArgument(0)));

        // When
        ReservaResponse response = reservaService.crearReserva(request);

        // Then
        assertNotNull(response);
        assertEquals(10L, response.horarioId());
        assertEquals(1L, response.miembroId());
        assertEquals("ACTIVA", response.estado());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando el miembro no existe")
    void crearReservaMiembroNoExiste() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 99L);
        when(memberClient.getMemberById(99L)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(request));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando member-service falla")
    void crearReservaMemberServiceFalla() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 99L);
        when(memberClient.getMemberById(99L)).thenThrow(new RuntimeException("servicio no disponible"));

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(request));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando el horario no existe")
    void crearReservaHorarioNoExiste() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 1L);
        when(memberClient.getMemberById(1L)).thenReturn(Map.of("id", 1L));
        when(classClient.getHorarioById(10L)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> reservaService.crearReserva(request));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar reserva duplicada activa")
    void crearReservaDuplicadaActiva() {
        // Given
        ReservaRequest request = new ReservaRequest(10L, 1L);
        when(memberClient.getMemberById(1L)).thenReturn(Map.of("id", 1L));
        when(classClient.getHorarioById(10L)).thenReturn(Map.of("id", 10L));
        when(reservaRepository.existsByHorarioIdAndMiembroIdAndEstado(10L, 1L, "ACTIVA")).thenReturn(true);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> reservaService.crearReserva(request));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe cancelar reserva activa cambiando estado a CANCELADA")
    void cancelarReservaActiva() {
        // Given
        Reserva reserva = reserva(1L, "ACTIVA");
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // When
        ReservaResponse response = reservaService.cancelarReserva(1L);

        // Then
        assertNotNull(response);
        assertEquals("CANCELADA", response.estado());
        verify(reservaRepository).save(reserva);
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException al cancelar reserva inexistente")
    void cancelarReservaNoEncontrada() {
        // Given
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> reservaService.cancelarReserva(99L));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar reservas por miembro")
    void listarReservasPorMiembro() {
        // Given
        when(reservaRepository.findByMiembroId(1L)).thenReturn(List.of(reserva(1L, "ACTIVA")));

        // When
        List<ReservaResponse> response = reservaService.obtenerReservasPorMiembro(1L);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).miembroId());
        verify(reservaRepository).findByMiembroId(1L);
    }

    @Test
    @DisplayName("Debe listar todas las reservas")
    void listarTodasLasReservas() {
        // Given
        when(reservaRepository.findAll()).thenReturn(List.of(reserva(1L, "ACTIVA"), reserva(2L, "CANCELADA")));

        // When
        List<ReservaResponse> response = reservaService.obtenerTodasLasReservas();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(reservaRepository).findAll();
    }

    private Reserva reserva(Long id, String estado) {
        return Reserva.builder()
                .id(id)
                .horarioId(10L)
                .miembroId(1L)
                .fechaReserva(LocalDateTime.now())
                .estado(estado)
                .build();
    }

    private Reserva reservaGuardada(Reserva reserva) {
        reserva.setId(1L);
        reserva.setEstado("ACTIVA");
        reserva.setFechaReserva(LocalDateTime.now());
        return reserva;
    }
}
