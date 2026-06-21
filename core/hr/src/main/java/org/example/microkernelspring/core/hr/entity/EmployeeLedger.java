package org.example.microkernelspring.core.hr.entity;

import jakarta.persistence.*;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LkpLedgerEntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(LkpLedgerEntryType entryType) {
        this.entryType = entryType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getReferenceInvoiceId() {
        return referenceInvoiceId;
    }

    public void setReferenceInvoiceId(UUID referenceInvoiceId) {
        this.referenceInvoiceId = referenceInvoiceId;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
