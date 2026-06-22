package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

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
@Data
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

}
