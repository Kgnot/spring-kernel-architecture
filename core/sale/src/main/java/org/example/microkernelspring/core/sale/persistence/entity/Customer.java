package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Clientes del tenant. El email/teléfono del cliente persona se consultan
 * vía identity.UserContact a través de profileId (referencia LÓGICA); no
 * se duplican aquí.
 */
@Entity
@Table(name = "customers", schema = "sale")
@Data
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas).
     */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /**
     * Referencia LÓGICA a identity.profile.id; nullable: clientes de
     * mostrador pueden no tener profile.
     */
    @Column(name = "profile_id")
    private UUID profileId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_type_id", nullable = false)
    private LkpCustomerType customerType;

    /**
     * Si customerType = company.
     */
    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    /**
     * Para ventas a crédito/fiado.
     */
    @Column(name = "credit_limit", precision = 14, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Customer() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }


}
