package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
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
public class LkpMovementType {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** purchase_in, sale_out, transfer_in, transfer_out, adjustment, return_in */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** +1 entra, -1 sale. */
    @Column(name = "direction", nullable = false)
    private short direction;

    public LkpMovementType() {
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

    public short getDirection() {
        return direction;
    }

    public void setDirection(short direction) {
        this.direction = direction;
    }
}
