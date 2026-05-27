package cl.duocuc.crmenesesn.paymentservice.controller;

import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;
import cl.duocuc.crmenesesn.paymentservice.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrarPago(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/member/{miembroId}")
    public ResponseEntity<List<PagoResponse>> obtenerPagosPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorMiembro(miembroId));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> obtenerTodosLosPagos() {
        return ResponseEntity.ok(pagoService.obtenerTodosLosPagos());
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<PagoResponse> anularPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.anularPago(id));
    }
}