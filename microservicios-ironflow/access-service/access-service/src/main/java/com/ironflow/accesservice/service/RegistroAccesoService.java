package com.ironflow.accesservice.service;

import com.ironflow.accesservice.client.MembershipClient;
import com.ironflow.accesservice.dto.RegistroAccesoRequest;
import com.ironflow.accesservice.dto.RegistroAccesoResponse;
import com.ironflow.accesservice.exception.RecursoNoEncontradoException;
import com.ironflow.accesservice.model.RegistroAcceso;
import com.ironflow.accesservice.repository.RegistroAccesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistroAccesoService {

    private final RegistroAccesoRepository registroAccesoRepository;
    private final MembershipClient membershipClient;

    @Transactional
    public RegistroAccesoResponse verificarAcceso(RegistroAccesoRequest request) {
        log.info("Verificando acceso para miembro ID: {}", request.getMiembroId());

        // Validar que el miembro tiene un plan activo en membership-service
        boolean membresiaActiva = false;
        try {
            Object planes = membershipClient.getPlanMiembroByMiembroId(request.getMiembroId());
            if (planes != null && !(planes instanceof List && ((List<?>) planes).isEmpty())) {
                membresiaActiva = true;
            }
        } catch (Exception e) {
            log.warn("No se encontró plan activo para miembro id: {}", request.getMiembroId());
            membresiaActiva = false;
        }

        RegistroAcceso registro = RegistroAcceso.builder()
                .miembroId(request.getMiembroId())
                .fechaAcceso(LocalDateTime.now())
                .observacion(request.getObservacion())
                .estado(membresiaActiva ? "PERMITIDO" : "DENEGADO")
                .build();

        RegistroAcceso guardado = registroAccesoRepository.save(registro);
        log.info("Acceso registrado con ID: {} y estado: {}", guardado.getId(), guardado.getEstado());

        return mapearRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<RegistroAccesoResponse> listarRegistros() {
        return registroAccesoRepository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegistroAccesoResponse buscarPorId(Long id) {
        RegistroAcceso registro = registroAccesoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un registro de acceso con id " + id));

        return mapearRespuesta(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroAccesoResponse> listarPorMiembro(Long miembroId) {
        return registroAccesoRepository.findByMiembroIdOrderByFechaAccesoDesc(miembroId)
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    private RegistroAccesoResponse mapearRespuesta(RegistroAcceso registro) {
        return new RegistroAccesoResponse(
                registro.getId(),
                registro.getMiembroId(),
                registro.getEstado(),
                registro.getFechaAcceso(),
                registro.getObservacion()
        );
    }
}