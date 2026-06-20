package com.ironflow.trainerservice.service;

import com.ironflow.trainerservice.dto.EntrenadorRequest;
import com.ironflow.trainerservice.dto.EntrenadorResponse;
import com.ironflow.trainerservice.exception.RecursoNoEncontradoException;
import com.ironflow.trainerservice.model.Entrenador;
import com.ironflow.trainerservice.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    @Transactional
    public EntrenadorResponse crearEntrenador(EntrenadorRequest request) {
        log.info("Registrando entrenador: {}", request.getNombre());

        if (entrenadorRepository.existsByCorreoIgnoreCase(request.getCorreo())) {
            log.warn("Ya existe un entrenador con correo: {}", request.getCorreo());
            throw new IllegalArgumentException("Ya existe un entrenador con el correo: " + request.getCorreo());
        }

        Entrenador entrenador = Entrenador.builder()
                .nombre(request.getNombre())
                .especialidad(request.getEspecialidad())
                .aniosExperiencia(request.getAniosExperiencia())
                .telefono(request.getTelefono())
                .correo(request.getCorreo())
                .estado(request.getEstado().toUpperCase())
                .activo("ACTIVO".equalsIgnoreCase(request.getEstado()))
                .build();

        Entrenador guardado = entrenadorRepository.save(entrenador);
        log.info("Entrenador registrado con ID: {}", guardado.getId());
        return mapearRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<EntrenadorResponse> listarEntrenadores() {
        log.info("Listando todos los entrenadores");
        return entrenadorRepository.findByActivoTrue()
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public EntrenadorResponse buscarPorId(Long id) {
        log.info("Buscando entrenador con id: {}", id);
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Entrenador no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("No existe un entrenador con id " + id);
                });
        return mapearRespuesta(entrenador);
    }

    @Transactional
    public EntrenadorResponse actualizarEntrenador(Long id, EntrenadorRequest request) {
        log.info("Actualizando entrenador con id: {}", id);
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Entrenador no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("No existe un entrenador con id " + id);
                });

        if (entrenador.getEstado().equals("INACTIVO")) {
            log.warn("Intento de actualizar entrenador INACTIVO con id: {}", id);
            throw new IllegalArgumentException("No se puede actualizar un entrenador INACTIVO");
        }
        if (entrenadorRepository.existsByCorreoIgnoreCaseAndIdNot(request.getCorreo(), id)) {
            throw new IllegalArgumentException("Ya existe un entrenador con el correo: " + request.getCorreo());
        }

        entrenador.setNombre(request.getNombre());
        entrenador.setEspecialidad(request.getEspecialidad());
        entrenador.setAniosExperiencia(request.getAniosExperiencia());
        entrenador.setTelefono(request.getTelefono());
        entrenador.setCorreo(request.getCorreo());
        entrenador.setEstado(request.getEstado().toUpperCase());
        entrenador.setActivo("ACTIVO".equalsIgnoreCase(request.getEstado()));

        Entrenador actualizado = entrenadorRepository.save(entrenador);
        log.info("Entrenador actualizado con ID: {}", actualizado.getId());
        return mapearRespuesta(actualizado);
    }

    @Transactional
    public void eliminarEntrenador(Long id) {
        log.info("Desactivando entrenador con id: {}", id);
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Entrenador no encontrado con id: {}", id);
                    return new RecursoNoEncontradoException("No existe un entrenador con id " + id);
                });
        entrenador.setEstado("INACTIVO");
        entrenador.setActivo(false);
        entrenadorRepository.save(entrenador);
        log.info("Entrenador desactivado con id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<EntrenadorResponse> listarPorEspecialidad(String especialidad) {
        log.info("Listando entrenadores por especialidad: {}", especialidad);
        return entrenadorRepository.findByEspecialidadIgnoreCaseAndActivoTrue(especialidad)
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    private EntrenadorResponse mapearRespuesta(Entrenador entrenador) {
        return new EntrenadorResponse(
                entrenador.getId(),
                entrenador.getNombre(),
                entrenador.getEspecialidad(),
                entrenador.getAniosExperiencia(),
                entrenador.getTelefono(),
                entrenador.getCorreo(),
                entrenador.getEstado(),
                entrenador.getActivo()
        );
    }
}
