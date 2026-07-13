package cl.duocuc.crmenesesn.bookingservice.service;

import cl.duocuc.crmenesesn.bookingservice.client.ClassClient;
import cl.duocuc.crmenesesn.bookingservice.client.MemberClient;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.model.Reserva;
import cl.duocuc.crmenesesn.bookingservice.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClassClient classClient;
    private final MemberClient memberClient;

    @Override
    @Transactional
    public ReservaResponse crearReserva(ReservaRequest request) {
        log.info("Procesando reserva para miembroId: {} en horarioId: {}", request.miembroId(), request.horarioId());

        // 1. Validar que el miembro existe en member-service
        try {
            Object miembro = memberClient.getMemberById(request.miembroId());
            if (miembro == null) {
                throw new EntityNotFoundException("No existe un miembro con id: " + request.miembroId());
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Miembro no encontrado con id: {}", request.miembroId());
            throw new EntityNotFoundException("No existe un miembro con id: " + request.miembroId());
        }

        // 2. Validar que el horario existe en class-service
        try {
            Object horario = classClient.getHorarioById(request.horarioId());
            if (horario == null) {
                log.warn("Horario no encontrado con id: {}", request.horarioId());
                throw new EntityNotFoundException("No existe un horario con id: " + request.horarioId());
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Horario no encontrado con id: {}", request.horarioId());
            throw new EntityNotFoundException("No existe un horario con id: " + request.horarioId());
        }

        // 3. Validar si el usuario ya tiene una reserva activa para esa clase/horario
        boolean existeReserva = reservaRepository.existsByHorarioIdAndMiembroIdAndEstado(
                request.horarioId(), request.miembroId(), "ACTIVA"
        );
        if (existeReserva) {
            throw new IllegalArgumentException("El cliente ya posee una reserva activa para esta clase.");
        }

        try {
            classClient.reservarCupo(request.horarioId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.warn("No fue posible reservar cupo para horario id: {}", request.horarioId());
            throw new IllegalArgumentException("No fue posible reservar cupo para el horario solicitado: " + e.getMessage());
        }

        Reserva nuevaReserva = Reserva.builder()
                .horarioId(request.horarioId())
                .miembroId(request.miembroId())
                .build();

        try {
            Reserva saved = reservaRepository.save(nuevaReserva);
            log.info("Reserva creada exitosamente con ID: {}", saved.getId());
            return mapToResponse(saved);
        } catch (RuntimeException e) {
            classClient.liberarCupo(request.horarioId());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> obtenerReservasPorMiembro(Long miembroId) {
        return reservaRepository.findByMiembroId(miembroId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> obtenerReservasPorHorario(Long horarioId) {
        return reservaRepository.findByHorarioId(horarioId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> obtenerTodasLasReservas() {
        return reservaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaResponse cancelarReserva(Long id) {
        log.info("Cancelando reserva con ID: {}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException("La reserva ya se encuentra cancelada.");
        }

        reserva.setEstado("CANCELADA");
        Reserva updated = reservaRepository.save(reserva);
        classClient.liberarCupo(reserva.getHorarioId());
        log.info("Reserva ID: {} cambiada a estado CANCELADA", id);
        return mapToResponse(updated);
    }

    private ReservaResponse mapToResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getHorarioId(),
                reserva.getMiembroId(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );
    }
}
