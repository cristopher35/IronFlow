package com.ironflow.equipmentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String categoria;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String ubicacion;
    @Column(nullable = false)
    private Integer cantidadDisponible;
    @Column(nullable = false)
    private Boolean mantenimientoRequerido;
}
