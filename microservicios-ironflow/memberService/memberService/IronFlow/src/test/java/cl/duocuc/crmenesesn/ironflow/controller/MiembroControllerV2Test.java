package cl.duocuc.crmenesesn.ironflow.controller;

import cl.duocuc.crmenesesn.ironflow.assemblers.MiembroModelAssembler;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MiembroControllerV2Test {

    private final MiembroService service = mock(MiembroService.class);
    private final MiembroModelAssembler assembler = mock(MiembroModelAssembler.class);
    private final MiembroControllerV2 controller = new MiembroControllerV2(service, assembler);

    @Test
    void delegatesHateoasEndpoints() {
        // Given
        MiembroRequest request = new MiembroRequest("Juan Perez", "12345678-9", "juan@gmail.com", "912345678");
        MiembroResponse response = new MiembroResponse(1L, "Juan Perez", "12345678-9", "juan@gmail.com", "912345678", "ACTIVO");
        EntityModel<MiembroResponse> model = EntityModel.of(response);
        when(service.obtenerTodosLosMiembros()).thenReturn(List.of(response));
        when(service.obtenerMiembroPorId(1L)).thenReturn(response);
        when(service.crearMiembro(request)).thenReturn(response);
        when(service.actualizarMiembro(1L, request)).thenReturn(response);
        when(assembler.toModel(response)).thenReturn(model);

        // When / Then
        assertEquals(1, controller.obtenerTodosLosMiembros().getContent().size());
        assertEquals(model, controller.obtenerMiembroPorId(1L));
        assertEquals(201, controller.crearMiembro(request).getStatusCode().value());
        assertEquals(model, controller.actualizarMiembro(1L, request));
        assertEquals(204, controller.eliminarMiembro(1L).getStatusCode().value());
        verify(service).eliminarMiembro(1L);
    }
}
