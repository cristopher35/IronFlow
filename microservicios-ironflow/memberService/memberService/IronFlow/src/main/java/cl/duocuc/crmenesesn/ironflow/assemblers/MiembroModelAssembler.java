package cl.duocuc.crmenesesn.ironflow.assemblers;

import cl.duocuc.crmenesesn.ironflow.controller.MiembroControllerV2;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MiembroModelAssembler implements RepresentationModelAssembler<MiembroResponse, EntityModel<MiembroResponse>> {

    @Override
    public EntityModel<MiembroResponse> toModel(MiembroResponse miembro) {
        return EntityModel.of(miembro,
                linkTo(methodOn(MiembroControllerV2.class).obtenerMiembroPorId(miembro.id())).withSelfRel(),
                linkTo(methodOn(MiembroControllerV2.class).obtenerTodosLosMiembros()).withRel("members"));
    }
}