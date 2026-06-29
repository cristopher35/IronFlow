package com.ironflow.notificationservice.repository;

import com.ironflow.notificationservice.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByCanalIgnoreCase(String canal);
    List<Notificacion> findByMiembroId(Long miembroId);
}
