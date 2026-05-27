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

        Equipo equipo = Equipo.builder()
                .nombre(request.getNombre())
                .categoria(request.getCategoria())
                .estado(request.getEstado())
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

        equipo.setNombre(request.getNombre());
        equipo.setCategoria(request.getCategoria());
        equipo.setEstado(request.getEstado());
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
