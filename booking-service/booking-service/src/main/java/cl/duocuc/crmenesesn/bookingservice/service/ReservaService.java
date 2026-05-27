package cl.duocuc.crmenesesn.bookingservice.service;

import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;

import java.util.List;

public interface ReservaService {

    ReservaResponse crearReserva(ReservaRequest request);
    ReservaResponse obtenerReservaPorId(Long id);
    List<ReservaResponse> obtenerReservasPorMiembro(Long miembroId);
    List<ReservaResponse> obtenerReservasPorHorario(Long horarioId);
    List<ReservaResponse> obtenerTodasLasReservas();
    ReservaResponse cancelarReserva(Long id);
}