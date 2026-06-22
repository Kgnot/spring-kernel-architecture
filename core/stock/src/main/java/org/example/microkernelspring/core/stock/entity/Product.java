package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

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
@Data
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
}
