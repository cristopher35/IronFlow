package com.ironflow.trainerservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entrenadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especialidad;

    @Column(nullable = false)
    private Integer aniosExperiencia;

    @Column
    private String telefono;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}