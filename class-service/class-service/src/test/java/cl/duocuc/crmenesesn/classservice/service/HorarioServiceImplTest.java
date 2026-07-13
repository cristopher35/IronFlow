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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceImplTest {
    @Mock HorarioRepository horarioRepository;
    @Mock TipoClaseRepository tipoClaseRepository;
    @Mock TrainerClient trainerClient;
    @InjectMocks HorarioServiceImpl service;

    @Test
    @DisplayName("Debe crear horario cuando clase y entrenador son validos")
    void creaHorario() {
        TipoClase tipo = tipoActivo();
        HorarioRequest request = request();
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(trainerClient.getTrainerById(2L)).thenReturn(new Object());
        when(horarioRepository.existsByEntrenadorIdAndFechaHoraAndEstado(2L, request.fechaHora(), "ACTIVO"))
                .thenReturn(false);
        when(horarioRepository.save(any(Horario.class))).thenAnswer(invocation -> {
            Horario horario = invocation.getArgument(0);
            horario.setId(10L);
            horario.setEstado("ACTIVO");
            horario.setAforoActual(0);
            return horario;
        });

        var response = service.crearHorario(request);

        assertEquals(10L, response.id());
        assertEquals("Yoga", response.tipoClase().nombre());
    }

    @Test
    @DisplayName("Debe fallar al crear horario con tipo inexistente")
    void fallaCrearConTipoInexistente() {
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.crearHorario(request()));
    }

    @Test
    @DisplayName("Debe fallar al crear horario si trainer-service no responde")
    void fallaCrearSiTrainerNoDisponible() {
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));
        when(trainerClient.getTrainerById(2L)).thenThrow(new RuntimeException("offline"));

        assertThrows(IllegalStateException.class, () -> service.crearHorario(request()));
    }

    @Test
    @DisplayName("Debe obtener horario por id y listar filtros")
    void obtieneYListaHorarios() {
        Horario horario = horarioActivo();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(horarioRepository.findAll()).thenReturn(List.of(horario));
        when(horarioRepository.findByTipoClaseId(1L)).thenReturn(List.of(horario));
        when(horarioRepository.findByEntrenadorId(2L)).thenReturn(List.of(horario));

        assertEquals(5L, service.obtenerHorarioPorId(5L).id());
        assertEquals(1, service.obtenerTodosLosHorarios().size());
        assertEquals(1, service.obtenerHorariosPorTipoClase(1L).size());
        assertEquals(1, service.obtenerHorariosPorEntrenador(2L).size());
    }

    @Test
    @DisplayName("Debe fallar al obtener horario inexistente")
    void fallaObtenerHorarioInexistente() {
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.obtenerHorarioPorId(99L));
    }

    @Test
    @DisplayName("Debe actualizar horario activo")
    void actualizaHorario() {
        Horario horario = horarioActivo();
        HorarioRequest request = new HorarioRequest(1L, 2L, LocalDateTime.now().plusDays(3), 30);
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));
        when(trainerClient.getTrainerById(2L)).thenReturn(new Object());
        when(horarioRepository.existsByEntrenadorIdAndFechaHoraAndEstadoAndIdNot(2L, request.fechaHora(), "ACTIVO", 5L))
                .thenReturn(false);
        when(horarioRepository.save(horario)).thenReturn(horario);

        var response = service.actualizarHorario(5L, request);

        assertEquals(30, response.aforoMax());
        assertEquals(request.fechaHora(), response.fechaHora());
    }

    @Test
    @DisplayName("Debe rechazar actualizar horario inactivo")
    void rechazaActualizarHorarioInactivo() {
        Horario horario = horarioActivo();
        horario.setEstado("INACTIVO");
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));

        assertThrows(IllegalArgumentException.class, () -> service.actualizarHorario(5L, request()));
    }

    @Test
    @DisplayName("Debe rechazar asignar tipo inactivo al actualizar")
    void rechazaActualizarConTipoInactivo() {
        TipoClase tipo = tipoActivo();
        tipo.setEstado("INACTIVO");
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horarioActivo()));
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));

        assertThrows(IllegalArgumentException.class, () -> service.actualizarHorario(5L, request()));
    }

    @Test
    @DisplayName("Debe rechazar conflicto de entrenador al actualizar")
    void rechazaConflictoEntrenadorAlActualizar() {
        HorarioRequest request = request();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horarioActivo()));
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));
        when(trainerClient.getTrainerById(2L)).thenReturn(new Object());
        when(horarioRepository.existsByEntrenadorIdAndFechaHoraAndEstadoAndIdNot(2L, request.fechaHora(), "ACTIVO", 5L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.actualizarHorario(5L, request));
    }

    @Test
    @DisplayName("Debe reservar cupo si hay disponibilidad")
    void reservaCupo() {
        Horario horario = horarioActivo();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(horarioRepository.save(horario)).thenReturn(horario);

        assertEquals(4, service.reservarCupo(5L).aforoActual());
    }

    @Test
    @DisplayName("Debe rechazar reservar en horario inactivo")
    void rechazaReservarHorarioInactivo() {
        Horario horario = horarioActivo();
        horario.setEstado("INACTIVO");
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));

        assertThrows(IllegalArgumentException.class, () -> service.reservarCupo(5L));
    }

    @Test
    @DisplayName("Debe liberar cupo sin bajar de cero")
    void liberaCupoEnCero() {
        Horario horario = horarioActivo();
        horario.setAforoActual(0);
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(horarioRepository.save(horario)).thenReturn(horario);

        assertEquals(0, service.liberarCupo(5L).aforoActual());
    }

    @Test
    @DisplayName("Debe desactivar horario")
    void eliminaHorario() {
        Horario horario = horarioActivo();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));

        service.eliminarHorario(5L);

        assertEquals("INACTIVO", horario.getEstado());
        verify(horarioRepository).save(horario);
    }

    @Test
    @DisplayName("Debe fallar al eliminar horario inexistente")
    void fallaEliminarInexistente() {
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.eliminarHorario(99L));
    }

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

    @Test
    @DisplayName("Debe rechazar reservar cupo cuando el horario esta lleno")
    void rechazaReservarCupoConAforoLleno() {
        TipoClase tipo = TipoClase.builder().id(1L).nombre("Yoga").estado("ACTIVO").build();
        Horario horario = Horario.builder().id(5L).tipoClase(tipo).entrenadorId(2L)
                .fechaHora(LocalDateTime.now().plusDays(1)).aforoMax(10).aforoActual(10).estado("ACTIVO").build();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));

        assertThrows(IllegalArgumentException.class, () -> service.reservarCupo(5L));
        verify(horarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe liberar un cupo cuando existe aforo actual")
    void liberaCupo() {
        TipoClase tipo = TipoClase.builder().id(1L).nombre("Yoga").estado("ACTIVO").build();
        Horario horario = Horario.builder().id(5L).tipoClase(tipo).entrenadorId(2L)
                .fechaHora(LocalDateTime.now().plusDays(1)).aforoMax(10).aforoActual(3).estado("ACTIVO").build();
        when(horarioRepository.findById(5L)).thenReturn(Optional.of(horario));
        when(horarioRepository.save(horario)).thenReturn(horario);

        assertEquals(2, service.liberarCupo(5L).aforoActual());
        verify(horarioRepository).save(horario);
    }

    private HorarioRequest request() {
        return new HorarioRequest(1L, 2L, LocalDateTime.now().plusDays(1), 20);
    }

    private TipoClase tipoActivo() {
        return TipoClase.builder().id(1L).nombre("Yoga").descripcion("Movilidad").estado("ACTIVO").build();
    }

    private Horario horarioActivo() {
        return Horario.builder().id(5L).tipoClase(tipoActivo()).entrenadorId(2L)
                .fechaHora(LocalDateTime.now().plusDays(1)).aforoMax(10).aforoActual(3).estado("ACTIVO").build();
    }
}
