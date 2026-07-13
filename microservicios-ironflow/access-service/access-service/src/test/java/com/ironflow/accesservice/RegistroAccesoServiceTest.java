package com.ironflow.accesservice;

import com.ironflow.accesservice.client.MembershipClient;
import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.exception.RecursoNoEncontradoException;
import com.ironflow.accesservice.model.RegistroAcceso;
import com.ironflow.accesservice.repository.RegistroAccesoRepository;
import com.ironflow.accesservice.service.RegistroAccesoService;
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
class RegistroAccesoServiceTest {

    @Mock
    private RegistroAccesoRepository registroAccesoRepository;

    @Mock
    private MembershipClient membershipClient;

    @InjectMocks
    private RegistroAccesoService registroAccesoService;

    @Test
    @DisplayName("Debe permitir acceso cuando existe plan activo")
    void accesoPermitido() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(1L)).thenReturn(List.of(Map.of("estado", "ACTIVO")));
        when(registroAccesoRepository.save(any(RegistroAcceso.class))).thenAnswer(invocation -> registroGuardado(invocation.getArgument(0), 1L));

        // When
        RegistroAccesoResponse response = registroAccesoService.verificarAcceso(request(1L));

        // Then
        assertNotNull(response);
        assertEquals("PERMITIDO", response.estado());
        assertEquals(1L, response.miembroId());
        verify(registroAccesoRepository).save(any(RegistroAcceso.class));
    }

    @Test
    @DisplayName("Debe denegar acceso cuando no existe plan activo")
    void accesoDenegadoSinPlanActivo() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(2L)).thenReturn(List.of());
        when(registroAccesoRepository.save(any(RegistroAcceso.class))).thenAnswer(invocation -> registroGuardado(invocation.getArgument(0), 2L));

        // When
        RegistroAccesoResponse response = registroAccesoService.verificarAcceso(request(2L));

        // Then
        assertNotNull(response);
        assertEquals("DENEGADO", response.estado());
        verify(registroAccesoRepository).save(any(RegistroAcceso.class));
    }

    @Test
    @DisplayName("Debe denegar acceso cuando membership-service falla")
    void accesoDenegadoPorFallaRemota() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(3L)).thenThrow(new RuntimeException("timeout"));
        when(registroAccesoRepository.save(any(RegistroAcceso.class))).thenAnswer(invocation -> registroGuardado(invocation.getArgument(0), 3L));

        // When
        RegistroAccesoResponse response = registroAccesoService.verificarAcceso(request(3L));

        // Then
        assertNotNull(response);
        assertEquals("DENEGADO", response.estado());
    }

    @Test
    @DisplayName("Debe buscar registro de acceso por ID")
    void buscarPorId() {
        // Given
        when(registroAccesoRepository.findById(1L)).thenReturn(Optional.of(registro(1L, "PERMITIDO")));

        // When
        RegistroAccesoResponse response = registroAccesoService.buscarPorId(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("PERMITIDO", response.estado());
        verify(registroAccesoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el registro no existe")
    void buscarPorIdNoEncontrado() {
        // Given
        when(registroAccesoRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecursoNoEncontradoException.class, () -> registroAccesoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Debe listar todos los registros de acceso")
    void listarTodos() {
        // Given
        when(registroAccesoRepository.findAll()).thenReturn(List.of(registro(1L, "PERMITIDO"), registro(2L, "DENEGADO")));

        // When
        List<RegistroAccesoResponse> response = registroAccesoService.listarRegistros();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        verify(registroAccesoRepository).findAll();
    }

    @Test
    @DisplayName("Debe listar registros por miembro")
    void listarPorMiembro() {
        // Given
        when(registroAccesoRepository.findByMiembroIdOrderByFechaAccesoDesc(1L)).thenReturn(List.of(registro(1L, "PERMITIDO")));

        // When
        List<RegistroAccesoResponse> response = registroAccesoService.listarPorMiembro(1L);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(registroAccesoRepository).findByMiembroIdOrderByFechaAccesoDesc(1L);
    }

    private RegistroAccesoRequest request(Long miembroId) {
        RegistroAccesoRequest request = new RegistroAccesoRequest();
        request.setMiembroId(miembroId);
        request.setObservacion("Entrada principal");
        return request;
    }

    private RegistroAcceso registro(Long id, String estado) {
        return RegistroAcceso.builder()
                .id(id)
                .miembroId(1L)
                .estado(estado)
                .fechaAcceso(LocalDateTime.now())
                .observacion("Entrada principal")
                .build();
    }

    private RegistroAcceso registroGuardado(RegistroAcceso registro, Long id) {
        registro.setId(id);
        return registro;
    }
}
