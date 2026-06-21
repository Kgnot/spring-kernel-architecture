package org.example.microkernelspring.core.identity.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Catálogo GLOBAL de permisos granulares del sistema, ej:
 * "sale.invoice.create", "stock.adjust".
 */
@Entity
@Table(
        name = "permission",
        schema = "identity",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class Permission {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "description", length = 255)
    private String description;

    public Permission() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
