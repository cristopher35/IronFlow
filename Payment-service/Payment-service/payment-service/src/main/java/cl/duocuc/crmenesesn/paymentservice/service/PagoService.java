package cl.duocuc.crmenesesn.paymentservice.service;

import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;

import java.util.List;

public interface PagoService {

    PagoResponse registrarPago(PagoRequest request);
    PagoResponse obtenerPagoPorId(Long id);
    List<PagoResponse> obtenerPagosPorMiembro(Long miembroId);
    List<PagoResponse> obtenerTodosLosPagos();
    PagoResponse anularPago(Long id);
}