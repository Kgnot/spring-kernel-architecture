package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Existencias reales: cuánto hay de cada producto, en cada sede
 * (siteId LÓGICO hacia tenant.sites, sin FK real entre schemas).
 */
@Entity
@Table(
        name = "inventory",
        schema = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "product_id", "site_id"})
)
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Referencia LÓGICA a tenant.sites.id (sin FK real entre schemas). */
    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Column(name = "quantity_on_hand", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantityOnHand = BigDecimal.ZERO;

    /** Reservado por ventas/pedidos en curso. */
    @Column(name = "quantity_reserved", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantityReserved = BigDecimal.ZERO;

    /** Umbral para alertar reabastecimiento. */
    @Column(name = "reorder_point", precision = 14, scale = 3)
    private BigDecimal reorderPoint;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Inventory() {
    }

    @PrePersist
    @PreUpdate
    protected void touch() {
        this.updatedAt = Instant.now();
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UUID getSiteId() {
        return siteId;
    }

    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }

    public BigDecimal getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(BigDecimal quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(BigDecimal quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public BigDecimal getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
