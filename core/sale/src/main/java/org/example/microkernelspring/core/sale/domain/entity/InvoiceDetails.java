package org.example.microkernelspring.core.sale.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Detalle de cada línea de la factura. Exactamente uno de productId /
 * service debe estar lleno (constraint a nivel de aplicación, o
 * CHECK (num_nonnulls(product_id, service_id) = 1) en Postgres si se
 * desea reforzar en base de datos) — esta pareja de columnas es el
 * discriminador, sin necesitar tabla padre ni herencia:
 *
 *   - productId es LÓGICO porque cruza al schema "stock" (sin FK real).
 *   - service es FK real porque Service vive en este mismo schema "sale".
 *
 * Ambos casos guardan snapshot de nombre/precio porque el catálogo de
 * origen puede cambiar después de vender y la factura histórica no debe
 * cambiar.
 */
@Entity
@Table(name = "invoice_details", schema = "sale")
@Data
public class InvoiceDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    /**
     * Referencia LÓGICA opcional a stock.product.id — se llena cuando la
     * línea es un producto inventariable. Mutuamente excluyente con
     * "service".
     */
    @Column(name = "product_id")
    private UUID productId;

    /**
     * FK real (mismo schema) — se llena cuando la línea es un servicio.
     * Mutuamente excluyente con "productId".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    /** Snapshot del nombre del ítem al momento de vender. */
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "quantity", nullable = false, precision = 14, scale = 3)
    private BigDecimal quantity = BigDecimal.ONE;

    /** Snapshot del precio al momento de vender. */
    @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", nullable = false, precision = 14, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "line_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal lineTotal;

    public InvoiceDetails() {
    }

    /**
     * Validación de invariante a nivel de aplicación: exactamente uno de
     * productId / service debe estar presente.
     */
    @PrePersist
    @PreUpdate
    private void validateDiscriminator() {
        boolean hasProduct = this.productId != null;
        boolean hasService = this.service != null;
        if (hasProduct == hasService) {
            throw new IllegalStateException(
                    "InvoiceDetails debe referenciar exactamente uno de productId o service, no ambos ni ninguno.");
        }
    }
}
