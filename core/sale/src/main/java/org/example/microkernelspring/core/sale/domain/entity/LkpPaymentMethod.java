package org.example.microkernelspring.core.sale.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Métodos de pago (cash, card, transfer, credit).
 */
@Entity
@Table(
        name = "lkp_payment_method",
        schema = "sale",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpPaymentMethod {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** cash, card, transfer, credit */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpPaymentMethod() {
    }

}
