package org.example.microkernelspring.core.sale.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Pagos aplicados a una factura. Permite pagos parciales o mixtos (ej.
 * mitad efectivo, mitad tarjeta) sin sobrecargar Invoice.
 */
@Entity
@Table(name = "payment", schema = "sale")
@Data
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** Referencia LÓGICA a tenant.tenants.id (sin FK real entre schemas). */
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private LkpPaymentMethod paymentMethod;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;

    /** Número de transacción/aprobación. */
    @Column(name = "reference_code", length = 100)
    private String referenceCode;

    public Payment() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.paidAt == null) {
            this.paidAt = Instant.now();
        }
    }

}
