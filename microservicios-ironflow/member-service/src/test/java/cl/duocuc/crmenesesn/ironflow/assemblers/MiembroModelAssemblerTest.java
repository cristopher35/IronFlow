package cl.duocuc.crmenesesn.ironflow.assemblers;

import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MiembroModelAssemblerTest {

    private final MiembroModelAssembler assembler = new MiembroModelAssembler();

    @Test
    void buildsEntityModelWithExpectedContentAndLinks() {
        // Given
        MiembroResponse response = new MiembroResponse(1L, "Juan Perez", "12345678-9", "juan@gmail.com", "912345678", "ACTIVO");

        // When
        EntityModel<MiembroResponse> model = assembler.toModel(response);

        // Then
        assertEquals(response, model.getContent());
        assertNotNull(model.getRequiredLink("self"));
        assertNotNull(model.getRequiredLink("members"));
    }
}
