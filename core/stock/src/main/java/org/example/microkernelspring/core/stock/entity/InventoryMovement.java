package org.example.microkernelspring.core.stock.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Kardex / historial de movimientos de inventario, para trazabilidad y
 * auditoría. inventory guarda el saldo actual; esta tabla guarda CADA
 * movimiento.
 */
@Entity
@Table(name = "inventory_movement", schema = "stock")
@Data
public class InventoryMovement {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas).
     */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movement_type_id", nullable = false)
    private LkpMovementType movementType;

    /**
     * Siempre positivo; la dirección (+/-) la da movementType.direction.
     */
    @Column(name = "quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantity;

    /**
     * Si el movimiento es una compra.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /**
     * Referencia LÓGICA polimórfica al documento origen, ej: sale.invoice.id.
     */
    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "notes", length = 255)
    private String notes;

    /**
     * Referencia LÓGICA a identity.users_login.id (sin FK real entre schemas).
     */
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public InventoryMovement() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
