package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Unidades de medida de producto (unit, kg, lt, box, m, etc.).
 */
@Entity
@Table(
        name = "lkp_unit_of_measure",
        schema = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpUnitOfMeasure {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** unit, kg, lt, box, m, etc. */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpUnitOfMeasure() {
    }
}
