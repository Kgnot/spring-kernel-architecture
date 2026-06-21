package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

/**
 * Tabla puente: qué plugins tiene contratados/activos cada tenant.
 * El backend consulta esto para saber si mostrar el módulo de tracking,
 * analítica, etc.
 *
 * El campo "config" es jsonb en Postgres; se mapea como String y se
 * serializa/deserializa en la capa de aplicación, o usando
 * @JdbcTypeCode(SqlTypes.JSON) de Hibernate 6 si se prefiere un mapeo
 * automático a Map/JsonNode.
 */
@Entity
@Table(
        name = "tenant_plugin",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "plugin_id"})
)
@Data
public class TenantPlugin {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plugin_id", nullable = false)
    private PluginCatalog plugin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private LkpPluginStatus status;

    @Column(name = "enabled_at", nullable = false)
    private Instant enabledAt;

    /** null = no expira / se renueva automáticamente */
    @Column(name = "expires_at")
    private Instant expiresAt;

    /** Configuración específica del tenant para ese plugin: límites, webhooks, claves. */
    @Column(name = "config", columnDefinition = "jsonb")
    private String config;

    public TenantPlugin() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.enabledAt == null) {
            this.enabledAt = Instant.now();
        }
    }
}
