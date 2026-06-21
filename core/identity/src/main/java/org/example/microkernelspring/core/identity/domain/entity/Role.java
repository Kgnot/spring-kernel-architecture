package org.example.microkernelspring.core.identity.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Roles tipo RBAC. Permite roles globales predefinidos (admin, employee,
 * viewer) cuando id es null, y roles custom por tenant cuando tiene
 * valor.
 */
@Entity
@Table(name = "role", schema = "identity")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas).
     * null = rol global del sistema (ej. super_admin).
     */
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    public Role() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
