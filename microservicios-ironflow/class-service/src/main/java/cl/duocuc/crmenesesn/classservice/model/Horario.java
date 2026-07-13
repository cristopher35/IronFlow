package cl.duocuc.crmenesesn.classservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_clase_id", nullable = false)
    private TipoClase tipoClase;

    @Column(nullable = false)
    private Long entrenadorId;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Integer aforoMax;

    @Column(nullable = false)
    @Builder.Default
    private Integer aforoActual = 0;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";
}