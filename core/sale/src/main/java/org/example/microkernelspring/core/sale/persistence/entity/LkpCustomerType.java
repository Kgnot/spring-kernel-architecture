package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
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
