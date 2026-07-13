package cl.duocuc.crmenesesn.classservice.service;

import cl.duocuc.crmenesesn.classservice.dto.HorarioRequest;
import cl.duocuc.crmenesesn.classservice.dto.HorarioResponse;

import java.util.List;

public interface HorarioService {

    HorarioResponse crearHorario(HorarioRequest request);
    HorarioResponse obtenerHorarioPorId(Long id);
    List<HorarioResponse> obtenerTodosLosHorarios();
    List<HorarioResponse> obtenerHorariosPorTipoClase(Long tipoClaseId);
    List<HorarioResponse> obtenerHorariosPorEntrenador(Long entrenadorId);
    HorarioResponse actualizarHorario(Long id, HorarioRequest request);
    HorarioResponse reservarCupo(Long id);
    HorarioResponse liberarCupo(Long id);
    void eliminarHorario(Long id);
}
