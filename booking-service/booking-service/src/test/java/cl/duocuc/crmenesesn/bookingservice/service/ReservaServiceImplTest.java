package cl.duocuc.crmenesesn.bookingservice.service;

import cl.duocuc.crmenesesn.bookingservice.client.ClassClient;
import cl.duocuc.crmenesesn.bookingservice.client.MemberClient;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.model.Reserva;
import cl.duocuc.crmenesesn.bookingservice.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository repository;

    @Mock
    private ClassClient classClient;

    @Mock
    private MemberClient memberClient;

    @InjectMocks
    private ReservaServiceImpl service;

    @Test
    @DisplayName("Debe crear reserva cuando miembro y horario existen")
    void creaReservaCuandoMiembroYHorarioExisten() {
        // Given
        when(memberClient.getMemberById(1L)).thenReturn(Map.of("id", 1L));
        when(classClient.getHorarioById(10L)).thenReturn(Map.of("id", 10L));
        when(repository.existsByHorarioIdAndMiembroIdAndEstado(10L, 1L, "ACTIVA")).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            Reserva reserva = invocation.getArgument(0);
            reserva.setId(1L);
            reserva.setFechaReserva(LocalDateTime.now());
            return reserva;
        });

        // When
        ReservaResponse response = service.crearReserva(new ReservaRequest(10L, 1L));

        // Then
        assertEquals("ACTIVA", response.estado());
        assertEquals(10L, response.horarioId());
        assertEquals(1L, response.miembroId());
        verify(repository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe rechazar reserva cuando miembro no existe")
    void rechazaReservaCuandoMiembroNoExiste() {
        // Given
        when(memberClient.getMemberById(99L)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> service.crearReserva(new ReservaRequest(10L, 99L)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar reserva duplicada activa")
    void rechazaReservaDuplicadaActiva() {
        // Given
        when(memberClient.getMemberById(1L)).thenReturn(Map.of("id", 1L));
        when(classClient.getHorarioById(10L)).thenReturn(Map.of("id", 10L));
        when(repository.existsByHorarioIdAndMiembroIdAndEstado(10L, 1L, "ACTIVA")).thenReturn(true);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> service.crearReserva(new ReservaRequest(10L, 1L)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe cancelar reserva activa")
    void cancelaReservaActiva() {
        // Given
        Reserva reserva = reserva("ACTIVA");
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));
        when(repository.save(reserva)).thenReturn(reserva);

        // When
        ReservaResponse response = service.cancelarReserva(1L);

        // Then
        assertEquals("CANCELADA", response.estado());
        verify(repository).save(reserva);
    }

    @Test
    @DisplayName("Debe rechazar cancelar reserva ya cancelada")
    void rechazaCancelarReservaYaCancelada() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(reserva("CANCELADA")));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> service.cancelarReserva(1L));
        verify(repository, never()).save(any());
    }

    private Reserva reserva(String estado) {
        return Reserva.builder()
                .id(1L)
                .horarioId(10L)
                .miembroId(1L)
                .fechaReserva(LocalDateTime.now())
                .estado(estado)
                .build();
    }
}
