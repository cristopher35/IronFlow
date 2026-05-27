package com.ironflow.branchservice.repository;

import com.ironflow.branchservice.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    List<Sucursal> findByCiudadIgnoreCase(String ciudad);
}
