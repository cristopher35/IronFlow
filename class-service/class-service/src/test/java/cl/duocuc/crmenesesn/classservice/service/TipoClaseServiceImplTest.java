package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.dto.TipoClaseRequest;
import cl.duocuc.crmenesesn.classservice.model.TipoClase;
import cl.duocuc.crmenesesn.classservice.repository.HorarioRepository;
import cl.duocuc.crmenesesn.classservice.repository.TipoClaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoClaseServiceImplTest {

    @Mock TipoClaseRepository tipoClaseRepository;
    @Mock HorarioRepository horarioRepository;
    @InjectMocks TipoClaseServiceImpl service;

    @Test
    @DisplayName("Debe crear tipo de clase cuando el nombre no existe")
    void creaTipoClase() {
        TipoClaseRequest request = new TipoClaseRequest("Yoga", "Movilidad");
        when(tipoClaseRepository.existsByNombreIgnoreCase("Yoga")).thenReturn(false);
        when(tipoClaseRepository.save(any(TipoClase.class))).thenAnswer(invocation -> {
            TipoClase tipo = invocation.getArgument(0);
            tipo.setId(1L);
            tipo.setEstado("ACTIVO");
            return tipo;
        });

        var response = service.crearTipoClase(request);

        assertEquals(1L, response.id());
        assertEquals("Yoga", response.nombre());
        verify(tipoClaseRepository).save(any(TipoClase.class));
    }

    @Test
    @DisplayName("Debe rechazar crear tipo de clase duplicado")
    void rechazaCrearDuplicado() {
        TipoClaseRequest request = new TipoClaseRequest("Yoga", "Movilidad");
        when(tipoClaseRepository.existsByNombreIgnoreCase("Yoga")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crearTipoClase(request));
        verify(tipoClaseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe obtener tipo de clase por id")
    void obtienePorId() {
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));

        var response = service.obtenerTipoClasePorId(1L);

        assertEquals("Yoga", response.nombre());
    }

    @Test
    @DisplayName("Debe fallar al buscar tipo inexistente")
    void fallaSiNoExiste() {
        when(tipoClaseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.obtenerTipoClasePorId(99L));
    }

    @Test
    @DisplayName("Debe listar tipos de clase")
    void listaTipos() {
        when(tipoClaseRepository.findAll()).thenReturn(List.of(tipoActivo()));

        assertEquals(1, service.obtenerTodosLosTiposClase().size());
    }

    @Test
    @DisplayName("Debe actualizar tipo activo sin nombre duplicado")
    void actualizaTipoActivo() {
        TipoClase tipo = tipoActivo();
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(tipoClaseRepository.existsByNombreIgnoreCaseAndIdNot("Pilates", 1L)).thenReturn(false);
        when(tipoClaseRepository.save(tipo)).thenReturn(tipo);

        var response = service.actualizarTipoClase(1L, new TipoClaseRequest("Pilates", "Core"));

        assertEquals("Pilates", response.nombre());
        assertEquals("Core", response.descripcion());
    }

    @Test
    @DisplayName("Debe rechazar actualizar tipo inactivo")
    void rechazaActualizarInactivo() {
        TipoClase tipo = tipoActivo();
        tipo.setEstado("INACTIVO");
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarTipoClase(1L, new TipoClaseRequest("Pilates", "Core")));
    }

    @Test
    @DisplayName("Debe rechazar actualizar con nombre usado por otro tipo")
    void rechazaActualizarNombreDuplicado() {
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));
        when(tipoClaseRepository.existsByNombreIgnoreCaseAndIdNot("Pilates", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarTipoClase(1L, new TipoClaseRequest("Pilates", "Core")));
    }

    @Test
    @DisplayName("Debe desactivar tipo sin horarios activos")
    void eliminaTipoSinHorariosActivos() {
        TipoClase tipo = tipoActivo();
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(horarioRepository.existsByTipoClaseIdAndEstado(1L, "ACTIVO")).thenReturn(false);

        service.eliminarTipoClase(1L);

        assertEquals("INACTIVO", tipo.getEstado());
        verify(tipoClaseRepository).save(tipo);
    }

    @Test
    @DisplayName("Debe rechazar baja cuando existen horarios activos")
    void rechazaEliminarConHorariosActivos() {
        when(tipoClaseRepository.findById(1L)).thenReturn(Optional.of(tipoActivo()));
        when(horarioRepository.existsByTipoClaseIdAndEstado(1L, "ACTIVO")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.eliminarTipoClase(1L));
    }

    private TipoClase tipoActivo() {
        return TipoClase.builder()
                .id(1L)
                .nombre("Yoga")
                .descripcion("Movilidad")
                .estado("ACTIVO")
                .build();
    }
}
