package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.dto.TipoClaseRequest;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseResponse;
import cl.duocuc.crmenesesn.classservice.model.TipoClase;
import cl.duocuc.crmenesesn.classservice.repository.HorarioRepository;
import cl.duocuc.crmenesesn.classservice.repository.TipoClaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoClaseServiceImpl implements TipoClaseService {

    private final TipoClaseRepository tipoClaseRepository;
    private final HorarioRepository horarioRepository;

    @Override
    public TipoClaseResponse crearTipoClase(TipoClaseRequest request) {
        log.info("Creando tipo de clase con nombre: {}", request.nombre());
        if (tipoClaseRepository.existsByNombreIgnoreCase(request.nombre())) {
            log.warn("Ya existe un tipo de clase con nombre: {}", request.nombre());
            throw new IllegalArgumentException("Ya existe un tipo de clase con el nombre: " + request.nombre());
        }
        TipoClase tipoClase = TipoClase.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .build();
        TipoClase saved = tipoClaseRepository.save(tipoClase);
        log.info("Tipo de clase creado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public TipoClaseResponse obtenerTipoClasePorId(Long id) {
        log.info("Buscando tipo de clase con id: {}", id);
        TipoClase tipoClase = tipoClaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tipo de clase no encontrado con id: {}", id);
                    return new NoSuchElementException("Tipo de clase no encontrado con id: " + id);
                });
        return toResponse(tipoClase);
    }

    @Override
    public List<TipoClaseResponse> obtenerTodosLosTiposClase() {
        log.info("Obteniendo todos los tipos de clase");
        return tipoClaseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TipoClaseResponse actualizarTipoClase(Long id, TipoClaseRequest request) {
        log.info("Actualizando tipo de clase con id: {}", id);
        TipoClase tipoClase = tipoClaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tipo de clase no encontrado con id: {}", id);
                    return new NoSuchElementException("Tipo de clase no encontrado con id: " + id);
                });
        if (tipoClase.getEstado().equals("INACTIVO")) {
            log.warn("Intento de actualizar tipo de clase INACTIVO con id: {}", id);
            throw new IllegalArgumentException("No se puede actualizar un tipo de clase INACTIVO");
        }
        if (tipoClaseRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre(), id)) {
            throw new IllegalArgumentException("Ya existe un tipo de clase con el nombre: " + request.nombre());
        }
        tipoClase.setNombre(request.nombre());
        tipoClase.setDescripcion(request.descripcion());
        TipoClase saved = tipoClaseRepository.save(tipoClase);
        log.info("Tipo de clase actualizado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void eliminarTipoClase(Long id) {
        log.info("Desactivando tipo de clase con id: {}", id);
        TipoClase tipoClase = tipoClaseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tipo de clase no encontrado con id: {}", id);
                    return new NoSuchElementException("Tipo de clase no encontrado con id: " + id);
                });
        if (horarioRepository.existsByTipoClaseIdAndEstado(id, "ACTIVO")) {
            log.warn("Intento de eliminar tipo de clase con horarios activos, id: {}", id);
            throw new IllegalArgumentException("No se puede eliminar un tipo de clase con horarios activos");
        }
        tipoClase.setEstado("INACTIVO");
        tipoClaseRepository.save(tipoClase);
        log.info("Tipo de clase desactivado con id: {}", id);
    }

    private TipoClaseResponse toResponse(TipoClase tipoClase) {
        return new TipoClaseResponse(
                tipoClase.getId(),
                tipoClase.getNombre(),
                tipoClase.getDescripcion(),
                tipoClase.getEstado()
        );
    }
}
