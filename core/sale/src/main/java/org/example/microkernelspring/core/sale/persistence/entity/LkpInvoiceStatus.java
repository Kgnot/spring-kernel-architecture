package org.example.microkernelspring.core.sale.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Estados posibles de una factura (draft, issued, paid, partially_paid,
 * cancelled, overdue).
 */
@Entity
@Table(
        name = "lkp_invoice_status",
        schema = "sale",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpInvoiceStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** draft, issued, paid, partially_paid, cancelled, overdue */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpInvoiceStatus() {
    }

}
