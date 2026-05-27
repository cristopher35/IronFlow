package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.client.TrainerClient;
import cl.duocuc.crmenesesn.classservice.dto.HorarioRequest;
import cl.duocuc.crmenesesn.classservice.dto.HorarioResponse;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseResponse;
import cl.duocuc.crmenesesn.classservice.model.Horario;
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
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final TipoClaseRepository tipoClaseRepository;
    private final TrainerClient trainerClient;

    @Override
    public HorarioResponse crearHorario(HorarioRequest request) {
        log.info("Creando horario para tipo de clase id: {}", request.tipoClaseId());

        TipoClase tipoClase = tipoClaseRepository.findById(request.tipoClaseId())
                .orElseThrow(() -> {
                    log.warn("Tipo de clase no encontrado con id: {}", request.tipoClaseId());
                    return new NoSuchElementException("Tipo de clase no encontrado con id: " + request.tipoClaseId());
                });

        if (tipoClase.getEstado().equals("INACTIVO")) {
            log.warn("Intento de crear horario para tipo de clase INACTIVO id: {}", request.tipoClaseId());
            throw new IllegalArgumentException("No se puede crear un horario para un tipo de clase INACTIVO");
        }

        // Validar que el entrenador existe en trainer-service
        try {
            trainerClient.getTrainerById(request.entrenadorId());
        } catch (Exception e) {
            log.warn("Entrenador no encontrado con id: {}", request.entrenadorId());
            throw new NoSuchElementException("No existe un entrenador con id: " + request.entrenadorId());
        }

        Horario horario = Horario.builder()
                .tipoClase(tipoClase)
                .entrenadorId(request.entrenadorId())
                .fechaHora(request.fechaHora())
                .aforoMax(request.aforoMax())
                .build();

        Horario saved = horarioRepository.save(horario);
        log.info("Horario creado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public HorarioResponse obtenerHorarioPorId(Long id) {
        log.info("Buscando horario con id: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new NoSuchElementException("Horario no encontrado con id: " + id);
                });
        return toResponse(horario);
    }

    @Override
    public List<HorarioResponse> obtenerTodosLosHorarios() {
        log.info("Obteniendo todos los horarios");
        return horarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<HorarioResponse> obtenerHorariosPorTipoClase(Long tipoClaseId) {
        log.info("Obteniendo horarios del tipo de clase id: {}", tipoClaseId);
        return horarioRepository.findByTipoClaseId(tipoClaseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<HorarioResponse> obtenerHorariosPorEntrenador(Long entrenadorId) {
        log.info("Obteniendo horarios del entrenador id: {}", entrenadorId);
        return horarioRepository.findByEntrenadorId(entrenadorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public HorarioResponse actualizarHorario(Long id, HorarioRequest request) {
        log.info("Actualizando horario con id: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new NoSuchElementException("Horario no encontrado con id: " + id);
                });
        if (horario.getEstado().equals("INACTIVO")) {
            log.warn("Intento de actualizar horario INACTIVO con id: {}", id);
            throw new IllegalArgumentException("No se puede actualizar un horario INACTIVO");
        }

        TipoClase tipoClase = tipoClaseRepository.findById(request.tipoClaseId())
                .orElseThrow(() -> new NoSuchElementException("Tipo de clase no encontrado con id: " + request.tipoClaseId()));

        horario.setTipoClase(tipoClase);
        horario.setEntrenadorId(request.entrenadorId());
        horario.setFechaHora(request.fechaHora());
        horario.setAforoMax(request.aforoMax());
        Horario saved = horarioRepository.save(horario);
        log.info("Horario actualizado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public void eliminarHorario(Long id) {
        log.info("Desactivando horario con id: {}", id);
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Horario no encontrado con id: {}", id);
                    return new NoSuchElementException("Horario no encontrado con id: " + id);
                });
        horario.setEstado("INACTIVO");
        horarioRepository.save(horario);
        log.info("Horario desactivado con id: {}", id);
    }

    private HorarioResponse toResponse(Horario horario) {
        TipoClase tipoClase = horario.getTipoClase();
        TipoClaseResponse tipoClaseResponse = new TipoClaseResponse(
                tipoClase.getId(),
                tipoClase.getNombre(),
                tipoClase.getDescripcion(),
                tipoClase.getEstado()
        );
        return new HorarioResponse(
                horario.getId(),
                tipoClaseResponse,
                horario.getEntrenadorId(),
                horario.getFechaHora(),
                horario.getAforoMax(),
                horario.getAforoActual(),
                horario.getEstado()
        );
    }
}