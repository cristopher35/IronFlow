package cl.duocuc.crmenesesn.membershipservice.repository;

import cl.duocuc.crmenesesn.membershipservice.model.PlanMiembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanMiembroRepository extends JpaRepository<PlanMiembro, Long> {

    List<PlanMiembro> findByMiembroId(Long miembroId);
    List<PlanMiembro> findByPlanId(Long planId);
    boolean existsByMiembroIdAndEstado(Long miembroId, String estado);
}