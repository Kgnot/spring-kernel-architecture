package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Catálogo de estados posibles de un tenant (trial, active, suspended, cancelled).
 * Tabla lookup en lugar de ENUM para poder agregar estados sin migrar tipos.
 */
@Entity
@Table(
        name = "lkp_tenant_status",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpTenantStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** trial, active, suspended, cancelled */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpTenantStatus() {
    }

}
