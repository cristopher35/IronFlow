package cl.duocuc.crmenesesn.ironflow.controller;

import cl.duocuc.crmenesesn.ironflow.assemblers.MiembroModelAssembler;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroRequest;
import cl.duocuc.crmenesesn.ironflow.dto.MiembroResponse;
import cl.duocuc.crmenesesn.ironflow.service.MiembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/members")
@RequiredArgsConstructor
@Tag(name = "Members V2", description = "Gestión de miembros con soporte HATEOAS")
public class MiembroControllerV2 {

    private final MiembroService miembroService;
    private final MiembroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los miembros", description = "Retorna lista de miembros con enlaces HATEOAS")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public CollectionModel<EntityModel<MiembroResponse>> obtenerTodosLosMiembros() {
        List<EntityModel<MiembroResponse>> miembros = miembroService.obtenerTodosLosMiembros()
                .stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(miembros,
                linkTo(methodOn(MiembroControllerV2.class).obtenerTodosLosMiembros()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener miembro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro encontrado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public EntityModel<MiembroResponse> obtenerMiembroPorId(
            @Parameter(description = "ID del miembro", required = true) @PathVariable Long id) {
        return assembler.toModel(miembroService.obtenerMiembroPorId(id));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo miembro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Miembro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<MiembroResponse>> crearMiembro(@Valid @RequestBody MiembroRequest request) {
        MiembroResponse created = miembroService.crearMiembro(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(created));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un miembro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Miembro actualizado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public EntityModel<MiembroResponse> actualizarMiembro(
            @PathVariable Long id, @Valid @RequestBody MiembroRequest request) {
        return assembler.toModel(miembroService.actualizarMiembro(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un miembro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Miembro eliminado"),
            @ApiResponse(responseCode = "404", description = "Miembro no encontrado")
    })
    public ResponseEntity<Void> eliminarMiembro(@PathVariable Long id) {
        miembroService.eliminarMiembro(id);
        return ResponseEntity.noContent().build();
    }
}