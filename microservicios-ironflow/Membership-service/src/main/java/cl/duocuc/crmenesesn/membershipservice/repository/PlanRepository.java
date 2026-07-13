package cl.duocuc.crmenesesn.membershipservice.repository;

import cl.duocuc.crmenesesn.membershipservice.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    boolean existsByNombre(String nombre);
}