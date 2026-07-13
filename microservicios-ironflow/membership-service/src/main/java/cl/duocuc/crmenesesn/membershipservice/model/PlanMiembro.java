package cl.duocuc.crmenesesn.membershipservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planes_miembros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanMiembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long miembroId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        this.creadoEn = LocalDateTime.now();
    }
}