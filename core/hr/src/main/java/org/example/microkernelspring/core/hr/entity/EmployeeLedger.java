package org.example.microkernelspring.core.hr.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Créditos y débitos prestados a empleados. Libro contable simple: cada
 * fila es un movimiento (préstamo o abono) y guarda el saldo resultante
 * para auditoría rápida.
 */
@Entity
@Table(name = "employee_ledger", schema = "hr")
@Data
public class EmployeeLedger {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_type_id", nullable = false)
    private LkpLedgerEntryType entryType;

    /** Siempre positivo; el signo lo da entryType (loan = débito, repayment = crédito). */
    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    /** Saldo acumulado de deuda tras este movimiento. */
    @Column(name = "balance_after", nullable = false, precision = 14, scale = 2)
    private BigDecimal balanceAfter;

    /** Ej: "préstamo nómina", "descuento por daño de inventario". */
    @Column(name = "reason", length = 255)
    private String reason;

    /**
     * Referencia LÓGICA opcional a sale.invoice.id, si el descuento viene
     * de una compra del empleado.
     */
    @Column(name = "reference_invoice_id")
    private UUID referenceInvoiceId;

    /** Referencia LÓGICA a identity.users_login.id (sin FK real entre schemas). */
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public EmployeeLedger() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
