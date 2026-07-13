package cl.duocuc.crmenesesn.paymentservice.controller;

import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;
import cl.duocuc.crmenesesn.paymentservice.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Operaciones de registro y gestión de pagos de membresías, incluyendo comunicación con membership-service")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago", description = "Registra un pago para un miembro, validando contra membership-service que el miembro tenga un plan activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "El miembro no tiene un plan activo")
    })
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrarPago(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pago por ID", description = "Retorna los datos de un pago específico según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/member/{miembroId}")
    @Operation(summary = "Obtener pagos de un miembro", description = "Retorna todos los pagos registrados para un miembro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<PagoResponse>> obtenerPagosPorMiembro(@PathVariable Long miembroId) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorMiembro(miembroId));
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos", description = "Retorna la lista completa de pagos registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    public ResponseEntity<List<PagoResponse>> obtenerTodosLosPagos() {
        return ResponseEntity.ok(pagoService.obtenerTodosLosPagos());
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular un pago", description = "Marca un pago existente como ANULADO. No permite anular un pago que ya está anulado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago anulado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "409", description = "El pago ya se encuentra anulado")
    })
    public ResponseEntity<PagoResponse> anularPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.anularPago(id));
    }
}