package org.example.microkernelspring.core.hr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Datos laborales del empleado. La identidad (nombre, documento, contacto)
 * vive en identity.Profile / identity.UserContact (referencia LÓGICA);
 * aquí solo lo específico de RRHH.
 */
@Entity
@Table(name = "employee", schema = "hr")
@Data
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /** Referencia LÓGICA a identity.profile.id (sin FK real entre schemas). */
    @Column(name = "profile_id", nullable = false)
    private UUID profileId;

    /** Referencia LÓGICA a tenant.sites.id, sede base donde trabaja. */
    @Column(name = "site_id")
    private UUID siteId;

    /** Cargo: cajero, vendedor, bodeguero, etc. */
    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "salary", precision = 14, scale = 2)
    private BigDecimal salary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employment_status_id", nullable = false)
    private LkpEmploymentStatus employmentStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Employee() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
