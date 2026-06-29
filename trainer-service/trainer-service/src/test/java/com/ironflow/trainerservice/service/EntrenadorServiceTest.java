package com.ironflow.trainerservice.service;

import com.ironflow.trainerservice.dto.EntrenadorRequest;
import com.ironflow.trainerservice.dto.EntrenadorResponse;
import com.ironflow.trainerservice.model.Entrenador;
import com.ironflow.trainerservice.repository.EntrenadorRepository;
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
class EntrenadorServiceTest {

    @Mock
    private EntrenadorRepository repository;

    @InjectMocks
    private EntrenadorService service;

    @Test
    @DisplayName("Debe crear entrenador con estado normalizado")
    void creaEntrenadorConEstadoNormalizado() {
        EntrenadorRequest request = request("activo", "trainer@ironflow.cl");
        when(repository.save(any())).thenAnswer(invocation -> {
            Entrenador entrenador = invocation.getArgument(0);
            entrenador.setId(1L);
            return entrenador;
        });

        EntrenadorResponse response = service.crearEntrenador(request);

        assertEquals("ACTIVO", response.estado());
        assertTrue(response.activo());
    }

    @Test
    @DisplayName("Debe listar solo entrenadores activos")
    void listaSoloEntrenadoresActivos() {
        when(repository.findByActivoTrue()).thenReturn(List.of(entrenador("ACTIVO", true)));

        List<EntrenadorResponse> response = service.listarEntrenadores();

        assertEquals(1, response.size());
        verify(repository).findByActivoTrue();
        verify(repository, never()).findAll();
    }

    @Test
    @DisplayName("Debe rechazar correo duplicado al actualizar entrenador")
    void rechazaCorreoDuplicadoAlActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(entrenador("ACTIVO", true)));
        when(repository.existsByCorreoIgnoreCaseAndIdNot("otro@ironflow.cl", 1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarEntrenador(1L, request("ACTIVO", "otro@ironflow.cl")));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar entrenador mediante baja lógica")
    void eliminaEntrenadorMedianteBajaLogica() {
        Entrenador entrenador = entrenador("ACTIVO", true);
        when(repository.findById(1L)).thenReturn(Optional.of(entrenador));

        service.eliminarEntrenador(1L);

        assertEquals("INACTIVO", entrenador.getEstado());
        assertFalse(entrenador.getActivo());
        verify(repository).save(entrenador);
    }

    private EntrenadorRequest request(String estado, String correo) {
        EntrenadorRequest request = new EntrenadorRequest();
        request.setNombre("Ana Trainer");
        request.setEspecialidad("Funcional");
        request.setAniosExperiencia(5);
        request.setTelefono("912345678");
        request.setCorreo(correo);
        request.setEstado(estado);
        return request;
    }

    private Entrenador entrenador(String estado, boolean activo) {
        return Entrenador.builder()
                .id(1L)
                .nombre("Ana Trainer")
                .especialidad("Funcional")
                .aniosExperiencia(5)
                .telefono("912345678")
                .correo("trainer@ironflow.cl")
                .estado(estado)
                .activo(activo)
                .build();
    }
}
