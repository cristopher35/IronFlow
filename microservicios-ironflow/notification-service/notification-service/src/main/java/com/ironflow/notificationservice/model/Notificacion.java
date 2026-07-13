package com.ironflow.notificationservice.model;

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

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long miembroId;
    @Column(nullable = false)
    private String canal;
    @Column(nullable = false)
    private String asunto;
    @Column(nullable = false, length = 2000)
    private String mensaje;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;
}
