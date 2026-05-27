package cl.duocuc.crmenesesn.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long miembroId;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    @Builder.Default
    private String estado = "ACTIVO";

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}