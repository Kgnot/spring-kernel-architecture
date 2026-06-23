package org.example.microkernelspring.core.sale.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.event.InvoiceCreatedEvent;
import org.example.microkernelspring.core.sale.persistence.entity.Customer;
import org.example.microkernelspring.core.sale.persistence.entity.Invoice;
import org.example.microkernelspring.core.sale.persistence.entity.LkpInvoiceStatus;
import org.example.microkernelspring.core.sale.persistence.repository.InvoiceRepository;
import org.example.microkernelspring.core.sale.usecase.command.CreateInvoiceCommand;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private final EventBus eventBus; // Dependencia colocada

    @Transactional
    public void execute(CreateInvoiceCommand command) {
        Invoice invoice = new Invoice();
        invoice.setTenantId(command.tenantId());
        invoice.setSiteId(command.siteId());

        Customer customer = new Customer();
        customer.setId(command.customerId());
        invoice.setCustomer(customer);

        invoice.setEmployeeId(command.employeeId());
        invoice.setInvoiceNumber(command.invoiceNumber());

        LkpInvoiceStatus status = new LkpInvoiceStatus();
        status.setId(command.statusId());
        invoice.setStatus(status);

        invoice.setSubtotal(command.subtotal());
        invoice.setTaxTotal(command.taxTotal());
        invoice.setDiscountTotal(command.discountTotal());
        invoice.setGrandTotal(command.grandTotal());
        invoice.setCurrency(command.currency());
        invoice.setIssuedAt(command.issuedAt());
        invoice.setDueAt(command.dueAt());

        Invoice savedInvoice = invoiceRepository.save(invoice);

        eventBus.publish(new InvoiceCreatedEvent(
                savedInvoice.getId(),
                savedInvoice.getTenantId(),
                savedInvoice.getInvoiceNumber(),
                savedInvoice.getGrandTotal()
        ));
    }
}