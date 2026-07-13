package cl.duocuc.crmenesesn.ironflow.service;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;

import java.util.List;

public interface MiembroService {

    MiembroResponse crearMiembro(MiembroRequest request);
    MiembroResponse obtenerMiembroPorId(Long id);
    List<MiembroResponse> obtenerTodosLosMiembros();
    MiembroResponse actualizarMiembro(Long id, MiembroRequest request);
    void eliminarMiembro(Long id);
}