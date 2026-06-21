package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Estados posibles de la relación tenant-plugin (active, suspended, cancelled, trial).
 */
@Entity
@Table(
        name = "lkp_plugin_status",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpPluginStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** active, suspended, cancelled, trial */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpPluginStatus() {
    }
}
