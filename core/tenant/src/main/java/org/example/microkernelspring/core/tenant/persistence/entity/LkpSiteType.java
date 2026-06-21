package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Tipos de sede (store, warehouse, hq, kitchen, other).
 * Tabla lookup para que cada tenant eventualmente pueda tener tipos propios
 * (ej. "patio_acopio") sin tocar un ENUM global.
 */
@Entity
@Table(
        name = "lkp_site_type",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpSiteType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** store, warehouse, hq, kitchen, other */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpSiteType() {
    }
}
