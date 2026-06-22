package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Tipos de movimiento de inventario (purchase_in, sale_out, transfer_in,
 * transfer_out, adjustment, return_in). El campo "direction" evita tener
 * que interpretar el signo a partir del code: +1 entra, -1 sale.
 */
@Entity
@Table(
        name = "lkp_movement_type",
        schema = "stock",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpMovementType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * purchase_in, sale_out, transfer_in, transfer_out, adjustment, return_in
     */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * +1 entra, -1 sale.
     */
    @Column(name = "direction", nullable = false)
    private short direction;

    public LkpMovementType() {
    }
}
