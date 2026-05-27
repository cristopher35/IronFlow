package cl.duocuc.crmenesesn.ironflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "miembros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Miembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String telefono;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";
}