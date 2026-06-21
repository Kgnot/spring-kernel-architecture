package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Catálogo maestro de productos físicos INVENTARIABLES únicamente.
 * No tiene (ni debe tener) un campo isService: Product y sale.Services son
 * entidades TOTALMENTE INDEPENDIENTES, sin tabla padre ni herencia, cada una
 * vive y se gestiona por completo dentro de su propio schema.
 * "Product" es la ficha del producto; stock.Inventory es cuánto hay
 * físicamente de cada uno.
 */
@Entity
@Table(
        name = "product",
        schema = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "sku"})
)
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "sku", nullable = false, length = 80)
    private String sku;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private LkpProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private LkpUnitOfMeasure unitOfMeasure;

    /** Último costo de compra. */
    @Column(name = "cost_price", precision = 14, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "sale_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Product() {
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public LkpProductCategory getCategory() {
        return category;
    }

    public void setCategory(LkpProductCategory category) {
        this.category = category;
    }

    public LkpUnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(LkpUnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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
