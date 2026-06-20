package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenant_plugins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantPlugin {

    @EmbeddedId
    private TenantPluginId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tenantId")
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pluginId")
    @JoinColumn(name = "plugin_id")
    private PluginCatalog plugin;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(columnDefinition = "jsonb")
    private String config;
}