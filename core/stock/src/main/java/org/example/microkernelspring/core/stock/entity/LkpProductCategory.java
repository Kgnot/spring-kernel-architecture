package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Categorías de producto normalizadas (antes era un varchar libre).
 * Jerárquica vía parentCategory, ej: Bebidas > Gaseosas.
 */
@Entity
@Table(
        name = "lkp_product_category",
        schema = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpProductCategory {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** Permite categorías jerárquicas, ej: Bebidas > Gaseosas. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private LkpProductCategory parentCategory;

    public LkpProductCategory() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LkpProductCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(LkpProductCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
}
