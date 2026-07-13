package cl.duocuc.crmenesesn.bookingservice.controller;

import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Reservas de clases y cancelación de reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    @Operation(summary = "Crear reserva", description = "Valida el miembro y el horario antes de registrar una reserva")
    public ResponseEntity<ReservaResponse> crearReserva(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.crearReserva(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    @Operation(summary = "Listar reservas por miembro")
    public ResponseEntity<List<ReservaResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorMiembro(miembroId));
    }

    @GetMapping("/horario/{horarioId}")
    @Operation(summary = "Listar reservas por horario")
    public ResponseEntity<List<ReservaResponse>> listarPorHorario(@PathVariable Long horarioId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorHorario(horarioId));
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas")
    public ResponseEntity<List<ReservaResponse>> listarTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodasLasReservas());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }
}
