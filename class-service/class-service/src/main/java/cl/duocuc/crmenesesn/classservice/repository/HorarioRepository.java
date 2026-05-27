package cl.duocuc.crmenesesn.classservice.repository;

import cl.duocuc.crmenesesn.classservice.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByTipoClaseId(Long tipoClaseId);
    List<Horario> findByEntrenadorId(Long entrenadorId);
    boolean existsByTipoClaseIdAndEstado(Long tipoClaseId, String estado);
}