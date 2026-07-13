package cl.duocuc.crmenesesn.classservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipos_clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";
}