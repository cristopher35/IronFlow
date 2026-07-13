package cl.duocuc.crmenesesn.bookingservice.repository;

import cl.duocuc.crmenesesn.bookingservice.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByMiembroId(Long miembroId);
    List<Reserva> findByHorarioId(Long horarioId);
    boolean existsByHorarioIdAndMiembroIdAndEstado(Long horarioId, Long miembroId, String estado);
    long countByHorarioIdAndEstado(Long horarioId, String estado);
}