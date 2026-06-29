package com.ironflow.branchservice.service;

import com.ironflow.branchservice.dto.SucursalRequest;
import com.ironflow.branchservice.dto.SucursalResponse;
import com.ironflow.branchservice.exception.RecursoNoEncontradoException;
import com.ironflow.branchservice.model.Sucursal;
import com.ironflow.branchservice.repository.SucursalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository repository;

    @InjectMocks
    private SucursalService service;

    @Test
    @DisplayName("Debe crear sucursal activa cuando estado es ACTIVA")
    void creaSucursalActivaCuandoEstadoEsActiva() {
        // Given
        when(repository.save(any())).thenAnswer(invocation -> {
            Sucursal sucursal = invocation.getArgument(0);
            sucursal.setId(1L);
            return sucursal;
        });

        // When
        SucursalResponse response = service.crearSucursal(request("ACTIVA"));

        // Then
        assertEquals("ACTIVA", response.estado());
        assertTrue(response.activa());
    }

    @Test
    @DisplayName("Debe crear sucursal inactiva cuando estado no es ACTIVA")
    void creaSucursalInactivaCuandoEstadoNoEsActiva() {
        // Given
        when(repository.save(any())).thenAnswer(invocation -> {
            Sucursal sucursal = invocation.getArgument(0);
            sucursal.setId(2L);
            return sucursal;
        });

        // When
        SucursalResponse response = service.crearSucursal(request("INACTIVA"));

        // Then
        assertFalse(response.activa());
    }

    @Test
    @DisplayName("Debe recalcular estado activo al actualizar sucursal")
    void actualizarSucursalRecalculaEstadoActivo() {
        // Given
        Sucursal sucursal = sucursal("INACTIVA", false);
        when(repository.findById(1L)).thenReturn(Optional.of(sucursal));
        when(repository.save(sucursal)).thenReturn(sucursal);

        // When
        SucursalResponse response = service.actualizarSucursal(1L, request("ACTIVA"));

        // Then
        assertTrue(response.activa());
        assertEquals("ACTIVA", response.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si sucursal no existe")
    void buscarPorIdLanzaExcepcionSiNoExiste() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(99L));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar sucursales por ciudad usando búsqueda ignore case")
    void listarPorCiudadUsaBusquedaIgnoreCase() {
        // Given
        when(repository.findByCiudadIgnoreCase("Santiago")).thenReturn(List.of(sucursal("ACTIVA", true)));

        // When
        List<SucursalResponse> response = service.listarPorCiudad("Santiago");

        // Then
        assertEquals(1, response.size());
        verify(repository).findByCiudadIgnoreCase("Santiago");
    }

    private SucursalRequest request(String estado) {
        SucursalRequest request = new SucursalRequest();
        request.setNombre("Sucursal Centro");
        request.setCiudad("Santiago");
        request.setDireccion("Av. Principal 123");
        request.setTelefono("221234567");
        request.setHorarioAtencion("08:00-22:00");
        request.setEstado(estado);
        return request;
    }

    private Sucursal sucursal(String estado, boolean activa) {
        return Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .ciudad("Santiago")
                .direccion("Av. Principal 123")
                .telefono("221234567")
                .horarioAtencion("08:00-22:00")
                .estado(estado)
                .activa(activa)
                .build();
    }
}
