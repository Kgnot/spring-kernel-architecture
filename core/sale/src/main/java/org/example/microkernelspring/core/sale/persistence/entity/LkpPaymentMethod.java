package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
