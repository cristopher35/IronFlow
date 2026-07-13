package cl.duocuc.crmenesesn.bookingservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long horarioId;

    @Column(nullable = false)
    private Long miembroId;

    @Column(nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVA";

    @PrePersist
    public void prePersist() {
        this.fechaReserva = LocalDateTime.now();
    }
}