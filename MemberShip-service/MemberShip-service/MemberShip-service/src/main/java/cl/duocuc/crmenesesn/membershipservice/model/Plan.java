package cl.duocuc.crmenesesn.membershipservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "planes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer diasDuracion;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";
}