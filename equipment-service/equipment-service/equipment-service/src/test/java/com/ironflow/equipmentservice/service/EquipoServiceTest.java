package com.ironflow.equipmentservice.service;

import com.ironflow.equipmentservice.dto.EquipoRequest;
import com.ironflow.equipmentservice.dto.EquipoResponse;
import com.ironflow.equipmentservice.model.Equipo;
import com.ironflow.equipmentservice.repository.EquipoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {
    @Mock EquipoRepository repository;
    @InjectMocks EquipoService service;

    @Test
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
    void rechazaStockDisponibleDuranteMantencion() {
        assertThrows(IllegalArgumentException.class,
                () -> service.crearEquipo(request("MANTENCION", 1, true)));
        verify(repository, never()).save(any());
    }

    @Test
    void rechazaActualizarEquipoInactivo() {
        Equipo equipo = equipo("INACTIVO", 0, false);
        when(repository.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarEquipo(1L, request("DISPONIBLE", 2, false)));
        verify(repository, never()).save(any());
    }

    @Test
    void bajaLogicaPoneStockEnCero() {
        Equipo equipo = equipo("DISPONIBLE", 4, false);
        when(repository.findById(1L)).thenReturn(Optional.of(equipo));

        service.eliminarEquipo(1L);

        assertEquals("INACTIVO", equipo.getEstado());
        assertEquals(0, equipo.getCantidadDisponible());
        verify(repository).save(equipo);
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
