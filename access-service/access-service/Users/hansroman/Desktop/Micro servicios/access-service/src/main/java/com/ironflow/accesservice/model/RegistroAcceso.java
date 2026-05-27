package com.ironflow.accesservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long miembroId;

    private String estado;

    private LocalDateTime fechaAcceso;

    private String observacion;
}
