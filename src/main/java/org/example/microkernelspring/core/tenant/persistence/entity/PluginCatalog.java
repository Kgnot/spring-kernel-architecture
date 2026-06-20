package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.microkernelspring.shared.kernel.PluginType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plugin_catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PluginCatalog {

    @Id
    @Column(length = 60)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PluginType tipo;

    @OneToMany(mappedBy = "plugin")
    private Set<TenantPlugin> tenants = new HashSet<>();
}