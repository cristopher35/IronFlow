package com.ironflow.equipmentservice.service;

import com.ironflow.equipmentservice.dto.EquipoRequest;
import com.ironflow.equipmentservice.dto.EquipoResponse;
import com.ironflow.equipmentservice.model.Equipo;
import com.ironflow.equipmentservice.repository.EquipoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {
    @Mock EquipoRepository repository;
    @InjectMocks EquipoService service;

    @Test
    @DisplayName("Debe crear equipo con estado normalizado")
    void creaEquipoConEstadoNormalizado() {
        when(repository.save(any())).thenAnswer(invocation -> {
            Equipo equipo = invocation.getArgument(0);
            equipo.setId(1L);
            return equipo;
        });

        EquipoResponse response = service.crearEquipo(request("disponible", 3, false));

        assertEquals("DISPONIBLE", response.estado());
    }

    @Test
    @DisplayName("Debe rechazar stock disponible durante mantención")
    void rechazaStockDisponibleDuranteMantencion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.crearEquipo(request("MANTENCION", 1, true)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar equipo disponible con mantenimiento requerido")
    void rechazaDisponibleConMantenimientoRequerido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.crearEquipo(request("DISPONIBLE", 1, true)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar todos los equipos")
    void listaTodosLosEquipos() {
        when(repository.findAll()).thenReturn(List.of(equipo("DISPONIBLE", 2, false)));

        List<EquipoResponse> response = service.listarEquipos();

        assertEquals(1, response.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Debe buscar equipo por id")
    void buscaEquipoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(equipo("DISPONIBLE", 2, false)));

        EquipoResponse response = service.buscarPorId(1L);

        assertEquals(1L, response.id());
        assertEquals("Caminadora", response.nombre());
    }

    @Test
    @DisplayName("Debe fallar al buscar equipo inexistente")
    void fallaBuscarEquipoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(com.ironflow.equipmentservice.exception.RecursoNoEncontradoException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    @DisplayName("Debe rechazar actualización de equipo inactivo")
    void rechazaActualizarEquipoInactivo() {
        Equipo equipo = equipo("INACTIVO", 0, false);
        when(repository.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarEquipo(1L, request("DISPONIBLE", 2, false)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar equipo activo")
    void actualizaEquipoActivo() {
        Equipo equipo = equipo("DISPONIBLE", 2, false);
        when(repository.findById(1L)).thenReturn(Optional.of(equipo));
        when(repository.save(equipo)).thenReturn(equipo);

        EquipoResponse response = service.actualizarEquipo(1L, request("MANTENCION", 0, true));

        assertEquals("MANTENCION", response.estado());
        assertEquals(0, response.cantidadDisponible());
        assertTrue(response.mantenimientoRequerido());
    }

    @Test
    @DisplayName("Debe listar equipos por estado")
    void listaEquiposPorEstado() {
        when(repository.findByEstadoIgnoreCase("DISPONIBLE")).thenReturn(List.of(equipo("DISPONIBLE", 2, false)));

        List<EquipoResponse> response = service.listarPorEstado("DISPONIBLE");

        assertEquals(1, response.size());
        verify(repository).findByEstadoIgnoreCase("DISPONIBLE");
    }

    @Test
    @DisplayName("Debe dejar stock en cero al realizar baja lógica")
    void bajaLogicaPoneStockEnCero() {
        Equipo equipo = equipo("DISPONIBLE", 4, false);
        when(repository.findById(1L)).thenReturn(Optional.of(equipo));

        service.eliminarEquipo(1L);

        assertEquals("INACTIVO", equipo.getEstado());
        assertEquals(0, equipo.getCantidadDisponible());
        verify(repository).save(equipo);
    }

    @Test
    @DisplayName("Debe fallar al eliminar equipo inexistente")
    void fallaEliminarEquipoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(com.ironflow.equipmentservice.exception.RecursoNoEncontradoException.class,
                () -> service.eliminarEquipo(99L));
        verify(repository, never()).save(any());
    }

    private EquipoRequest request(String estado, int cantidad, boolean mantenimiento) {
        EquipoRequest request = new EquipoRequest();
        request.setNombre("Caminadora");
        request.setCategoria("Cardio");
        request.setEstado(estado);
        request.setUbicacion("Sala 1");
        request.setCantidadDisponible(cantidad);
        request.setMantenimientoRequerido(mantenimiento);
        return request;
    }

    private Equipo equipo(String estado, int cantidad, boolean mantenimiento) {
        return Equipo.builder().id(1L).nombre("Caminadora").categoria("Cardio")
                .estado(estado).ubicacion("Sala 1").cantidadDisponible(cantidad)
                .mantenimientoRequerido(mantenimiento).build();
    }
}
