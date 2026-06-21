package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Table(name = "supplier", schema = "stock")
@Data
public class Supplier {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public Supplier() {
    }
}
