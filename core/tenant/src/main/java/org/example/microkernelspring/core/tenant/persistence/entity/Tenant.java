package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * La entidad raíz del sistema. Todo dato tenant-scoped cuelga de aquí
 * (por id lógico/real según el schema). El subdomain o un header
 * X-Tenant-Id resuelve quién está pidiendo qué.
 */
@Entity
@Table(
        name = "tenants",
        schema = "tenant",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "tax_id"),
                @UniqueConstraint(columnNames = "subdomain")
        }
)
@Data
public class Tenant {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Razón social */
    @Column(name = "legal_name", nullable = false, length = 255)
    private String legalName;

    /** Nombre comercial / marca */
    @Column(name = "trade_name", length = 255)
    private String tradeName;

    /** NIT / RUT / identificación fiscal */
    @Column(name = "tax_id", nullable = false, length = 50)
    private String taxId;

    /**
     * hacia lkp_industry.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id")
    private LkpIndustry industry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private LkpTenantStatus status;

    /** usado para resolver el tenant en el login: subdomain.app.com */
    @Column(name = "subdomain", nullable = false, length = 100)
    private String subdomain;

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone = "America/Bogota";

    /** ISO 4217 */
    @Column(name = "default_currency", nullable = false, length = 3)
    private String defaultCurrency = "COP";

    @Column(name = "trial_ends_at")
    private Instant trialEndsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** soft delete */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Tenant() {
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
