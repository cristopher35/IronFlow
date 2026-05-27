package com.ironflow.branchservice.service;

import com.ironflow.branchservice.dto.SucursalRequest;
import com.ironflow.branchservice.dto.SucursalResponse;
import com.ironflow.branchservice.exception.RecursoNoEncontradoException;
import com.ironflow.branchservice.model.Sucursal;
import com.ironflow.branchservice.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    @Transactional
    public SucursalResponse crearSucursal(SucursalRequest request) {
        log.info("Registrando sucursal: {}", request.getNombre());

        Sucursal sucursal = Sucursal.builder()
                .nombre(request.getNombre())
                .ciudad(request.getCiudad())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .horarioAtencion(request.getHorarioAtencion())
                .estado(request.getEstado())
                .activa("ACTIVA".equalsIgnoreCase(request.getEstado()))
                .build();

        Sucursal guardada = sucursalRepository.save(sucursal);

        log.info("Sucursal registrada con ID: {}", guardada.getId());
        return mapearRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<SucursalResponse> listarSucursales() {
        return sucursalRepository.findAll()
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public SucursalResponse buscarPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con id " + id));

        return mapearRespuesta(sucursal);
    }

    @Transactional
    public SucursalResponse actualizarSucursal(Long id, SucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una sucursal con id " + id));

        sucursal.setNombre(request.getNombre());
        sucursal.setCiudad(request.getCiudad());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setTelefono(request.getTelefono());
        sucursal.setHorarioAtencion(request.getHorarioAtencion());
        sucursal.setEstado(request.getEstado());
        sucursal.setActiva("ACTIVA".equalsIgnoreCase(request.getEstado()));

        Sucursal actualizada = sucursalRepository.save(sucursal);
        log.info("Sucursal actualizada con ID: {}", actualizada.getId());

        return mapearRespuesta(actualizada);
    }

    @Transactional(readOnly = true)
    public List<SucursalResponse> listarPorCiudad(String ciudad) {
        return sucursalRepository.findByCiudadIgnoreCase(ciudad)
                .stream()
                .map(this::mapearRespuesta)
                .toList();
    }

    private SucursalResponse mapearRespuesta(Sucursal sucursal) {
        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getCiudad(),
                sucursal.getDireccion(),
                sucursal.getTelefono(),
                sucursal.getHorarioAtencion(),
                sucursal.getEstado(),
                sucursal.getActiva()
        );
    }
}
