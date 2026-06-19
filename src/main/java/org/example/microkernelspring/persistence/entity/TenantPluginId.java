package org.example.microkernelspring.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TenantPluginId implements Serializable {

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "plugin_id")
    private String pluginId;
}