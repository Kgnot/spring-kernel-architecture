package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Servicios facturables (mano de obra, consultoría, citas, etc.), entidad
 * TOTALMENTE INDEPENDIENTE de stock.Product: sin tabla padre, sin
 * productId, sin relación lógica entre schemas. Un producto es siempre
 * inventariable (vive en stock); un servicio nunca lo es (vive solo en
 * sale). InvoiceDetails decide cuál de los dos se facturó mediante sus
 * columnas productId / serviceId, sin necesitar herencia.
 */
@Entity
@Table(
        name = "services",
        schema = "sale",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "code"})
)
@Data
public class Service {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /** Identificador propio del servicio, equivalente al sku de Product pero independiente. */
    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "price", nullable = false, precision = 14, scale = 2)
    private BigDecimal price;

    /** Útil para servicios agendables, ej: peluquería, consultoría. */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Service() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
