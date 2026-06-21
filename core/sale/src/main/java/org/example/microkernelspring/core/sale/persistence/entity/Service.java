package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
