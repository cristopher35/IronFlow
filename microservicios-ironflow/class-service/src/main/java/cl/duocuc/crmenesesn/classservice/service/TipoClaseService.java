package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.dto.TipoClaseRequest;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseResponse;

import java.util.List;

public interface TipoClaseService {

    TipoClaseResponse crearTipoClase(TipoClaseRequest request);
    TipoClaseResponse obtenerTipoClasePorId(Long id);
    List<TipoClaseResponse> obtenerTodosLosTiposClase();
    TipoClaseResponse actualizarTipoClase(Long id, TipoClaseRequest request);
    void eliminarTipoClase(Long id);
}