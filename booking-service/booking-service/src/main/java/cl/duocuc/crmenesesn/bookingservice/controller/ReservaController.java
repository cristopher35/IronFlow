package cl.duocuc.crmenesesn.bookingservice.controller;

import cl.duocuc.crmenesesn.bookingservice.dto.ReservaRequest;
import cl.duocuc.crmenesesn.bookingservice.dto.ReservaResponse;
import cl.duocuc.crmenesesn.bookingservice.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = reservaService.crearReserva(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(id));
    }

    @GetMapping("/miembro/{miembroId}")
    public ResponseEntity<List<ReservaResponse>> listarPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorMiembro(miembroId));
    }

    @GetMapping("/horario/{horarioId}")
    public ResponseEntity<List<ReservaResponse>> listarPorHorario(@PathVariable Long horarioId) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorHorario(horarioId));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodasLasReservas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservaResponse> cancelarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }
}