package cl.duocuc.crmenesesn.classservice.repository;

import cl.duocuc.crmenesesn.classservice.model.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoClaseRepository extends JpaRepository<TipoClase, Long> {

    boolean existsByNombre(String nombre);
}