package cl.duocuc.crmenesesn.ironflow.service;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import cl.duocuc.crmenesesn.ironflow.repository.MiembroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiembroServiceImpl implements MiembroService {

    private final MiembroRepository miembroRepository;

    @Override
    public MiembroResponse crearMiembro(MiembroRequest request) {
        log.info("Creando miembro con RUT: {}", request.rut());
        if (miembroRepository.existsByRut(request.rut())) {
            log.warn("Ya existe un miembro con RUT: {}", request.rut());
            throw new IllegalArgumentException("Ya existe un miembro con el RUT: " + request.rut());
        }
        if (miembroRepository.existsByEmail(request.email())) {
            log.warn("Ya existe un miembro con email: {}", request.email());
            throw new IllegalArgumentException("Ya existe un miembro con el email: " + request.email());
        }
        Miembro miembro = Miembro.builder()
                .nombre(request.nombre())
                .rut(request.rut())
                .email(request.email())
                .telefono(request.telefono())
                .build();
        Miembro saved = miembroRepository.save(miembro);
        log.info("Miembro creado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public MiembroResponse obtenerMiembroPorId(Long id) {
        log.info("Buscando miembro con id: {}", id);
        Miembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Miembro no encontrado con id: {}", id);
                    return new NoSuchElementException("Miembro no encontrado con id: " + id);
                });
        return toResponse(miembro);
    }

    @Override
    public List<MiembroResponse> obtenerTodosLosMiembros() {
        log.info("Obteniendo todos los miembros");
        return miembroRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MiembroResponse actualizarMiembro(Long id, MiembroRequest request) {
        log.info("Actualizando miembro con id: {}", id);
        Miembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Miembro no encontrado con id: {}", id);
                    return new NoSuchElementException("Miembro no encontrado con id: " + id);
                });
        if (miembro.getEstado().equals("INACTIVO")) {
            log.warn("Intento de actualizar miembro INACTIVO con id: {}", id);
            throw new IllegalArgumentException("No se puede actualizar un miembro INACTIVO");
        }
        miembro.setNombre(request.nombre());
        miembro.setRut(request.rut());
        miembro.setEmail(request.email());
        miembro.setTelefono(request.telefono());
        Miembro saved = miembroRepository.save(miembro);
        log.info("Miembro actualizado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void eliminarMiembro(Long id) {
        log.info("Desactivando miembro con id: {}", id);
        Miembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Miembro no encontrado con id: {}", id);
                    return new NoSuchElementException("Miembro no encontrado con id: " + id);
                });
        miembro.setEstado("INACTIVO");
        miembroRepository.save(miembro);
        log.info("Miembro desactivado con id: {}", id);
    }

    private MiembroResponse toResponse(Miembro miembro) {
        return new MiembroResponse(
                miembro.getId(),
                miembro.getNombre(),
                miembro.getRut(),
                miembro.getEmail(),
                miembro.getTelefono(),
                miembro.getEstado()
        );
    }
}