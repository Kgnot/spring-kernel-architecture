package org.example.microkernelspring.core.sale.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Cabecera de factura/venta. Los montos vienen agregados aquí; el detalle
 * línea por línea está en InvoiceDetails.
 */
@Entity
@Table(
        name = "invoice",
        schema = "sale",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "invoice_number"})
)
@Data
public class Invoice {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    /** Referencia LÓGICA a tenant.sites.id (sin FK real entre schemas). */
    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /** Referencia LÓGICA a hr.employee.id, quién vendió/atendió. */
    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private LkpInvoiceStatus status;

    @Column(name = "subtotal", nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxTotal = BigDecimal.ZERO;

    @Column(name = "discount_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal discountTotal = BigDecimal.ZERO;

    @Column(name = "grand_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "COP";

    @Column(name = "issued_at")
    private Instant issuedAt;

    /** Para ventas a crédito. */
    @Column(name = "due_at")
    private Instant dueAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Invoice() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
