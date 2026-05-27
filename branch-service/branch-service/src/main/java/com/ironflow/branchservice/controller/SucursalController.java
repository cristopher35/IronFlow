package com.ironflow.branchservice.controller;

import com.ironflow.branchservice.dto.SucursalRequest;
import com.ironflow.branchservice.dto.SucursalResponse;
import com.ironflow.branchservice.service.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<SucursalResponse> crearSucursal(@Valid @RequestBody SucursalRequest request) {
        SucursalResponse respuesta = sucursalService.crearSucursal(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/sucursales/{id}")
                .buildAndExpand(respuesta.id())
                .toUri();

        return ResponseEntity.created(location).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listarSucursales() {
        return ResponseEntity.ok(sucursalService.listarSucursales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizarSucursal(@PathVariable Long id, @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.actualizarSucursal(id, request));
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<SucursalResponse>> listarPorCiudad(@PathVariable String ciudad) {
        return ResponseEntity.ok(sucursalService.listarPorCiudad(ciudad));
    }
}
