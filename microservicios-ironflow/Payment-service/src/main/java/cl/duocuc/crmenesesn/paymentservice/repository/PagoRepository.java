package cl.duocuc.crmenesesn.paymentservice.repository;

import cl.duocuc.crmenesesn.paymentservice.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByMiembroId(Long miembroId);
    boolean existsByMiembroIdAndEstado(Long miembroId, String estado);
}