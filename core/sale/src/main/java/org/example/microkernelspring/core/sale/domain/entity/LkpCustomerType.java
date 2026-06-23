package org.example.microkernelspring.core.sale.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Tipos de cliente (person, company).
 */
@Entity
@Table(
        name = "lkp_customer_type",
        schema = "sale",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpCustomerType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** person, company */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpCustomerType() {
    }
}
