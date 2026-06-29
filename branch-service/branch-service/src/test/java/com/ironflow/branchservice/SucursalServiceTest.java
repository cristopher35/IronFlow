package com.ironflow.branchservice;

import com.ironflow.branchservice.dto.SucursalRequest;
import com.ironflow.branchservice.dto.SucursalResponse;
import com.ironflow.branchservice.exception.RecursoNoEncontradoException;
import com.ironflow.branchservice.model.Sucursal;
import com.ironflow.branchservice.repository.SucursalRepository;
import com.ironflow.branchservice.service.SucursalService;
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
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Test
    @DisplayName("Debe crear sucursal y retornar respuesta")
    void crearSucursal() {
        // Given
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(invocation -> {
            Sucursal sucursal = invocation.getArgument(0);
            sucursal.setId(1L);
            return sucursal;
        });

        // When
        SucursalResponse response = sucursalService.crearSucursal(request("ACTIVA"));

        // Then
        assertNotNull(response);
        assertEquals("Sucursal Centro", response.nombre());
        assertEquals("ACTIVA", response.estado());
        assertEquals(Boolean.TRUE, response.activa());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Debe listar todas las sucursales")
    void listarSucursales() {
        // Given
        when(sucursalRepository.findAll()).thenReturn(List.of(sucursal(1L, "ACTIVA")));

        // When
        List<SucursalResponse> response = sucursalService.listarSucursales();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(sucursalRepository).findAll();
    }

    @Test
    @DisplayName("Debe buscar sucursal por ID")
    void buscarPorId() {
        // Given
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal(1L, "ACTIVA")));

        // When
        SucursalResponse response = sucursalService.buscarPorId(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(sucursalRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si sucursal no existe")
    void buscarPorIdNoExiste() {
        // Given
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecursoNoEncontradoException.class, () -> sucursalService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Debe actualizar sucursal existente")
    void actualizarSucursal() {
        // Given
        Sucursal actual = sucursal(1L, "INACTIVA");
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(actual));
        when(sucursalRepository.save(actual)).thenReturn(actual);

        // When
        SucursalResponse response = sucursalService.actualizarSucursal(1L, request("ACTIVA"));

        // Then
        assertNotNull(response);
        assertEquals("ACTIVA", response.estado());
        assertEquals(Boolean.TRUE, response.activa());
        verify(sucursalRepository).save(actual);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException al actualizar sucursal inexistente")
    void actualizarSucursalNoExiste() {
        // Given
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecursoNoEncontradoException.class, () -> sucursalService.actualizarSucursal(99L, request("ACTIVA")));
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe listar sucursales por ciudad ignorando mayúsculas")
    void listarPorCiudad() {
        // Given
        when(sucursalRepository.findByCiudadIgnoreCase("Santiago")).thenReturn(List.of(sucursal(1L, "ACTIVA")));

        // When
        List<SucursalResponse> response = sucursalService.listarPorCiudad("Santiago");

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(sucursalRepository).findByCiudadIgnoreCase("Santiago");
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

    private Sucursal sucursal(Long id, String estado) {
        return Sucursal.builder()
                .id(id)
                .nombre("Sucursal Centro")
                .ciudad("Santiago")
                .direccion("Av. Principal 123")
                .telefono("221234567")
                .horarioAtencion("08:00-22:00")
                .estado(estado)
                .activa("ACTIVA".equalsIgnoreCase(estado))
                .build();
    }
}
