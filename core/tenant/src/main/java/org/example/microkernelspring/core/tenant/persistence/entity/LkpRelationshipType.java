package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Tipos de relación posibles entre dos tenants aliados
 * (shared_tracking, marketplace_supplier, franchise). Usado por TenantPartnerLink.
 */
@Entity
@Table(
        name = "lkp_relationship_type",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpRelationshipType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** shared_tracking, marketplace_supplier, franchise */
    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpRelationshipType() {
    }
}
