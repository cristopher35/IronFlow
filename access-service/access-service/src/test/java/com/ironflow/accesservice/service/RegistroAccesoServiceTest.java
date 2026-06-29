package com.ironflow.accesservice.service;

import com.ironflow.accesservice.client.MembershipClient;
import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.exception.RecursoNoEncontradoException;
import com.ironflow.accesservice.model.RegistroAcceso;
import com.ironflow.accesservice.repository.RegistroAccesoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroAccesoServiceTest {

    @Mock
    private RegistroAccesoRepository repository;

    @Mock
    private MembershipClient membershipClient;

    @InjectMocks
    private RegistroAccesoService service;

    @Test
    @DisplayName("Debe permitir acceso cuando existe membresía activa")
    void permiteAccesoCuandoExisteMembresiaActiva() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(1L)).thenReturn(List.of(Map.of("estado", "ACTIVO")));
        when(repository.save(any())).thenAnswer(invocation -> {
            RegistroAcceso registro = invocation.getArgument(0);
            registro.setId(1L);
            return registro;
        });

        // When
        RegistroAccesoResponse response = service.verificarAcceso(request(1L));

        // Then
        assertEquals("PERMITIDO", response.estado());
        assertEquals(1L, response.miembroId());
        assertNotNull(response.fechaAcceso());
    }

    @Test
    @DisplayName("Debe denegar acceso cuando no hay membresía activa")
    void deniegaAccesoCuandoNoHayMembresiaActiva() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(2L)).thenReturn(List.of());
        when(repository.save(any())).thenAnswer(invocation -> {
            RegistroAcceso registro = invocation.getArgument(0);
            registro.setId(2L);
            return registro;
        });

        // When
        RegistroAccesoResponse response = service.verificarAcceso(request(2L));

        // Then
        assertEquals("DENEGADO", response.estado());
        verify(repository).save(any(RegistroAcceso.class));
    }

    @Test
    @DisplayName("Debe denegar acceso cuando servicio remoto falla")
    void deniegaAccesoCuandoServicioRemotoFalla() {
        // Given
        when(membershipClient.getPlanMiembroByMiembroId(3L)).thenThrow(new IllegalStateException("timeout"));
        when(repository.save(any())).thenAnswer(invocation -> {
            RegistroAcceso registro = invocation.getArgument(0);
            registro.setId(3L);
            return registro;
        });

        // When
        RegistroAccesoResponse response = service.verificarAcceso(request(3L));

        // Then
        assertEquals("DENEGADO", response.estado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si registro de acceso no existe")
    void buscarPorIdLanzaExcepcionSiNoExiste() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RecursoNoEncontradoException.class, () -> service.buscarPorId(99L));
    }

    private RegistroAccesoRequest request(Long miembroId) {
        RegistroAccesoRequest request = new RegistroAccesoRequest();
        request.setMiembroId(miembroId);
        request.setObservacion("Entrada principal");
        return request;
    }
}
