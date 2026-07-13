package cl.duocuc.crmenesesn.ironflow.repository;

import cl.duocuc.crmenesesn.ironflow.model.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {

    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    boolean existsByRutAndIdNot(String rut, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
