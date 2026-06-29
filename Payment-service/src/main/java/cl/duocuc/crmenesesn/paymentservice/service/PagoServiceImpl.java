package cl.duocuc.crmenesesn.paymentservice.service;

import cl.duocuc.crmenesesn.paymentservice.client.MembershipClient;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoRequest;
import cl.duocuc.crmenesesn.paymentservice.dto.PagoResponse;
import cl.duocuc.crmenesesn.paymentservice.model.Pago;
import cl.duocuc.crmenesesn.paymentservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final MembershipClient membershipClient;

    @Override
    public PagoResponse registrarPago(PagoRequest request) {
        log.info("Registrando pago para miembro id: {}", request.miembroId());

        try {
            Object planMiembros = membershipClient.getPlanMiembroByMiembroId(request.miembroId());
            if (planMiembros == null || (planMiembros instanceof List && ((List<?>) planMiembros).isEmpty())) {
                log.warn("No se encontró plan activo para miembro id: {}", request.miembroId());
                throw new NoSuchElementException("No existe un plan activo para el miembro con id: " + request.miembroId());
            }
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Error al consultar plan para miembro id: {}", request.miembroId());
            throw new NoSuchElementException("No existe un plan activo para el miembro con id: " + request.miembroId());
        }

        Pago pago = Pago.builder()
                .miembroId(request.miembroId())
                .monto(request.monto())
                .metodoPago(request.metodoPago())
                .build();

        Pago saved = pagoRepository.save(pago);
        log.info("Pago registrado con id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    public PagoResponse obtenerPagoPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pago no encontrado con id: {}", id);
                    return new NoSuchElementException("Pago no encontrado con id: " + id);
                });
        return toResponse(pago);
    }

    @Override
    public List<PagoResponse> obtenerPagosPorMiembro(Long miembroId) {
        log.info("Obteniendo pagos del miembro con id: {}", miembroId);
        return pagoRepository.findByMiembroId(miembroId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PagoResponse> obtenerTodosLosPagos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PagoResponse anularPago(Long id) {
        log.info("Anulando pago con id: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pago no encontrado con id: {}", id);
                    return new NoSuchElementException("Pago no encontrado con id: " + id);
                });
        if (pago.getEstado().equals("ANULADO")) {
            log.warn("Intento de anular pago ya anulado con id: {}", id);
            throw new IllegalArgumentException("El pago ya se encuentra anulado");
        }
        pago.setEstado("ANULADO");
        Pago saved = pagoRepository.save(pago);
        log.info("Pago anulado con id: {}", saved.getId());
        return toResponse(saved);
    }

    private PagoResponse toResponse(Pago pago) {
        return new PagoResponse(
                pago.getId(),
                pago.getMiembroId(),
                pago.getMonto(),
                pago.getFechaPago(),
                pago.getMetodoPago(),
                pago.getEstado()
        );
    }
}