package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * Permite que dos tenants distintos se vinculen, por ejemplo para compartir
 * el mismo servicio de tracking o red logística. Ambas puntas de la relación
 * (tenant y partnerTenant) son FK reales porque tenants vive en este mismo
 * schema.
 */
@Entity
@Table(
        name = "tenant_partner_link",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "partner_tenant_id"})
)
@Data
public class TenantPartnerLink {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Tenant que inicia la relación.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * Tenant aliado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_tenant_id", nullable = false)
    private Tenant partnerTenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "relationship_type_id", nullable = false)
    private LkpRelationshipType relationshipType;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public TenantPartnerLink() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
