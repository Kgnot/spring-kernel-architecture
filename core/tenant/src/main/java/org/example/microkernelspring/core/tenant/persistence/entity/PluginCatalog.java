package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Catálogo GLOBAL (sin id) de módulos/plugins que la plataforma ofrece,
 * ej: tracking de pedidos, analítica avanzada, facturación electrónica.
 * No se borra, se desactiva (isActive = false).
 */
@Entity
@Table(
        name = "plugin_catalog",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class PluginCatalog {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Slug interno, ej: "tracking", "advanced_analytics", "multi_currency"
     */
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private LkpPluginCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_model_id")
    private LkpPricingModel pricingModel;

    @Column(name = "base_price", precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public PluginCatalog() {
    }
}
