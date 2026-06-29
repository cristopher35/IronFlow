package com.ironflow.equipmentservice.service;

import com.ironflow.equipmentservice.dto.EquipoRequest;
import com.ironflow.equipmentservice.dto.EquipoResponse;
import com.ironflow.equipmentservice.exception.RecursoNoEncontradoException;
import com.ironflow.equipmentservice.model.Equipo;
import com.ironflow.equipmentservice.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipoService {

    private final EquipoRepository equipoRepository;

    @Transactional
    public EquipoResponse crearEquipo(EquipoRequest request) {
        log.info("Registrando equipo: {}", request.getNombre());
        validarConsistencia(request);

        Equipo equipo = Equipo.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .estado(request.getEstado().toUpperCase())
                .ubicacion(request.getUbicacion())
                .cantidadDisponible(request.getCantidadDisponible())
                .mantenimientoRequerido(request.getMantenimientoRequerido())
                .build();

        Equipo guardado = equipoRepository.save(equipo);

        log.info("Equipo registrado con ID: {}", guardado.getId());
        return mapearRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<EquipoResponse> listarEquipos() {
        return equipoRepository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipoResponse buscarPorId(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un equipo con id " + id));

        return mapearRespuesta(equipo);
    }

    @Transactional
    public EquipoResponse actualizarEquipo(Long id, EquipoRequest request) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un equipo con id " + id));

        if ("INACTIVO".equals(equipo.getEstado())) {
            throw new IllegalArgumentException("No se puede actualizar un equipo INACTIVO");
        }
        validarConsistencia(request);

        equipo.setNombre(request.getNombre());
        equipo.setCategoria(request.getCategoria());
        equipo.setEstado(request.getEstado().toUpperCase());
        equipo.setUbicacion(request.getUbicacion());
        equipo.setCantidadDisponible(request.getCantidadDisponible());
        equipo.setMantenimientoRequerido(request.getMantenimientoRequerido());

        Equipo actualizado = equipoRepository.save(equipo);
        log.info("Equipo actualizado con ID: {}", actualizado.getId());

        return mapearRespuesta(actualizado);
    }

    @Transactional(readOnly = true)
    public List<EquipoResponse> listarPorEstado(String estado) {
        return equipoRepository.findByEstadoIgnoreCase(estado)
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional
    public void eliminarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un equipo con id " + id));
        equipo.setEstado("INACTIVO");
        equipo.setCantidadDisponible(0);
        equipoRepository.save(equipo);
        log.info("Equipo desactivado con ID: {}", id);
    }

    private void validarConsistencia(EquipoRequest request) {
        String estado = request.getEstado().toUpperCase();
        if (("MANTENCION".equals(estado) || "INACTIVO".equals(estado))
                && request.getCantidadDisponible() > 0) {
            throw new IllegalArgumentException("Un equipo en mantención o inactivo no puede tener stock disponible");
        }
        if ("MANTENCION".equals(estado) && !request.getMantenimientoRequerido()) {
            throw new IllegalArgumentException("El estado MANTENCION requiere marcar mantenimientoRequerido");
        }
        if ("DISPONIBLE".equals(estado) && request.getMantenimientoRequerido()) {
            throw new IllegalArgumentException("Un equipo DISPONIBLE no puede requerir mantenimiento");
        }
    }

    private EquipoResponse mapearRespuesta(Equipo equipo) {
        return new EquipoResponse(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getCategoria(),
                equipo.getEstado(),
                equipo.getUbicacion(),
                equipo.getCantidadDisponible(),
                equipo.getMantenimientoRequerido()
        );
    }
}
