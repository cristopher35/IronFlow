package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.client.TrainerClient;
import cl.duocuc.crmenesesn.classservice.dto.HorarioRequest;
import cl.duocuc.crmenesesn.classservice.model.Horario;
import cl.duocuc.crmenesesn.classservice.model.TipoClase;
import cl.duocuc.crmenesesn.classservice.repository.HorarioRepository;
import cl.duocuc.crmenesesn.classservice.repository.TipoClaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceImplTest {
    @Mock HorarioRepository horarioRepository;
    @Mock TipoClaseRepository tipoClaseRepository;
    @Mock TrainerClient trainerClient;
    @InjectMocks HorarioServiceImpl service;

    @Test
    @DisplayName("Debe rechazar creación de horario con clase inactiva")
    void rechazaClaseInactivaAlCrear() {
        TipoClase tipo = TipoClase.builder().id(1L).nombre("Yoga").estado("INACTIVO").build();
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));

        assertThrows(IllegalArgumentException.class, () -> service.crearHorario(request()));
        verifyNoInteractions(trainerClient);
    }

    @Test
    @DisplayName("Debe rechazar horario por conflicto de entrenador")
    void rechazaConflictoDeEntrenador() {
        TipoClase tipo = TipoClase.builder().id(1L).nombre("Yoga").estado("ACTIVO").build();
        HorarioRequest request = request();
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(trainerClient.getTrainerById(2L)).thenReturn(new Object());
        when(horarioRepository.existsByEntrenadorIdAndFechaHoraAndEstado(2L, request.fechaHora(), "ACTIVO"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crearHorario(request));
        verify(horarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar reducir aforo por debajo de reservas actuales")
    void rechazaReducirAforoPorDebajoDeReservas() {
        TipoClase tipo = TipoClase.builder().id(1L).nombre("Yoga").estado("ACTIVO").build();
        Horario horario = Horario.builder().id(5L).tipoClase(tipo).entrenadorId(2L)
                .fechaHora(LocalDateTime.now().plusDays(1)).aforoMax(20).aforoActual(10).estado("ACTIVO").build();
        HorarioRequest request = new HorarioRequest(1L, 2L, LocalDateTime.now().plusDays(2), 5);
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));

        assertThrows(IllegalArgumentException.class, () -> service.actualizarHorario(5L, request));
        verify(horarioRepository, never()).save(any());
    }

    private HorarioRequest request() {
        return new HorarioRequest(1L, 2L, LocalDateTime.now().plusDays(1), 20);
    }
}
