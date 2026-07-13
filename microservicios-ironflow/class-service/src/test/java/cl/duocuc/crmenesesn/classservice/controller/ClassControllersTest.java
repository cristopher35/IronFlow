package cl.duocuc.crmenesesn.classservice.controller;

import cl.duocuc.crmenesesn.classservice.dto.HorarioRequest;
import cl.duocuc.crmenesesn.classservice.dto.HorarioResponse;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseRequest;
import cl.duocuc.crmenesesn.classservice.dto.TipoClaseResponse;
import cl.duocuc.crmenesesn.classservice.service.HorarioService;
import cl.duocuc.crmenesesn.classservice.service.TipoClaseService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClassControllersTest {

    private final TipoClaseService tipoClaseService = mock(TipoClaseService.class);
    private final HorarioService horarioService = mock(HorarioService.class);
    private final TipoClaseController tipoController = new TipoClaseController(tipoClaseService);
    private final HorarioController horarioController = new HorarioController(horarioService);

    @Test
    void tipoClaseControllerDelegatesCrud() {
        TipoClaseRequest request = new TipoClaseRequest("Yoga", "Movilidad");
        TipoClaseResponse response = new TipoClaseResponse(1L, "Yoga", "Movilidad", "ACTIVO");
        when(tipoClaseService.crearTipoClase(request)).thenReturn(response);
        when(tipoClaseService.obtenerTipoClasePorId(1L)).thenReturn(response);
        when(tipoClaseService.obtenerTodosLosTiposClase()).thenReturn(List.of(response));
        when(tipoClaseService.actualizarTipoClase(1L, request)).thenReturn(response);

        assertEquals(201, tipoController.crearTipoClase(request).getStatusCode().value());
        assertEquals(response, tipoController.obtenerTipoClasePorId(1L).getBody());
        assertEquals(1, tipoController.obtenerTodosLosTiposClase().getBody().size());
        assertEquals(response, tipoController.actualizarTipoClase(1L, request).getBody());
        assertEquals(204, tipoController.eliminarTipoClase(1L).getStatusCode().value());
        verify(tipoClaseService).eliminarTipoClase(1L);
    }

    @Test
    void horarioControllerDelegatesCrudAndCapacityActions() {
        LocalDateTime fecha = LocalDateTime.now().plusDays(1);
        HorarioRequest request = new HorarioRequest(1L, 2L, fecha, 20);
        TipoClaseResponse tipo = new TipoClaseResponse(1L, "Yoga", "Movilidad", "ACTIVO");
        HorarioResponse response = new HorarioResponse(1L, tipo, 2L, fecha, 20, 0, "ACTIVO");
        when(horarioService.crearHorario(request)).thenReturn(response);
        when(horarioService.obtenerHorarioPorId(1L)).thenReturn(response);
        when(horarioService.obtenerTodosLosHorarios()).thenReturn(List.of(response));
        when(horarioService.obtenerHorariosPorTipoClase(1L)).thenReturn(List.of(response));
        when(horarioService.obtenerHorariosPorEntrenador(2L)).thenReturn(List.of(response));
        when(horarioService.actualizarHorario(1L, request)).thenReturn(response);
        when(horarioService.reservarCupo(1L)).thenReturn(response);
        when(horarioService.liberarCupo(1L)).thenReturn(response);

        assertEquals(201, horarioController.crearHorario(request).getStatusCode().value());
        assertEquals(response, horarioController.obtenerHorarioPorId(1L).getBody());
        assertEquals(1, horarioController.obtenerTodosLosHorarios().getBody().size());
        assertEquals(1, horarioController.obtenerHorariosPorTipoClase(1L).getBody().size());
        assertEquals(1, horarioController.obtenerHorariosPorEntrenador(2L).getBody().size());
        assertEquals(response, horarioController.actualizarHorario(1L, request).getBody());
        assertEquals(response, horarioController.reservarCupo(1L).getBody());
        assertEquals(response, horarioController.liberarCupo(1L).getBody());
        assertEquals(204, horarioController.eliminarHorario(1L).getStatusCode().value());
        verify(horarioService).eliminarHorario(1L);
    }
}
