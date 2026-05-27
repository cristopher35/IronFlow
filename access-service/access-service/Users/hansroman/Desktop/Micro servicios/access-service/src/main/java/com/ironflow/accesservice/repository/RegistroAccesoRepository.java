package com.ironflow.accesservice.repository;

import com.ironflow.accesservice.model.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {
    List<RegistroAcceso> findByMiembroIdOrderByFechaAccesoDesc(Long miembroId);
}
