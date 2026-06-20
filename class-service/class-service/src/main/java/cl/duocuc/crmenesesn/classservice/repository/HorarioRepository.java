package cl.duocuc.crmenesesn.classservice.repository;

import cl.duocuc.crmenesesn.classservice.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByTipoClaseId(Long tipoClaseId);
    List<Horario> findByEntrenadorId(Long entrenadorId);
    boolean existsByTipoClaseIdAndEstado(Long tipoClaseId, String estado);
    boolean existsByEntrenadorIdAndFechaHoraAndEstado(Long entrenadorId, LocalDateTime fechaHora, String estado);
    boolean existsByEntrenadorIdAndFechaHoraAndEstadoAndIdNot(Long entrenadorId, LocalDateTime fechaHora, String estado, Long id);
}
