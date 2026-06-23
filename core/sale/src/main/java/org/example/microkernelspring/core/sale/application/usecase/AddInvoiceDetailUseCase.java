package org.example.microkernelspring.core.sale.application.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.InvoiceDetailsRepository;
import org.example.microkernelspring.core.sale.application.repository.InvoiceRepository;
import org.example.microkernelspring.core.sale.application.usecase.command.AddInvoiceDetailCommand;
import org.example.microkernelspring.core.sale.domain.entity.Invoice;
import org.example.microkernelspring.core.sale.domain.entity.InvoiceDetails;
import org.example.microkernelspring.core.sale.domain.entity.Service;
import org.example.microkernelspring.core.sale.domain.event.InvoiceDetailAddedEvent;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AddInvoiceDetailUseCase {

    private final InvoiceDetailsRepository invoiceDetailsRepository;
    private final InvoiceRepository invoiceRepository;
    private final EventBus eventBus;

    @Transactional
    public void execute(AddInvoiceDetailCommand command) {
        InvoiceDetails detail = new InvoiceDetails();

        Invoice invoice = invoiceRepository.findById(command.invoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        detail.setInvoice(invoice);

        // Validación de discriminador manejada por la entidad en PrePersist
        detail.setProductId(command.productId());

        if (command.serviceId() != null) {
            Service service = new Service();
            service.setId(command.serviceId());
            detail.setService(service);
        }

        detail.setDescription(command.description());
        detail.setQuantity(command.quantity());
        detail.setUnitPrice(command.unitPrice());
        detail.setDiscount(command.discount());
        detail.setTaxRate(command.taxRate());
        detail.setLineTotal(command.lineTotal());

        InvoiceDetails savedDetail = invoiceDetailsRepository.save(detail);

        eventBus.publish(new InvoiceDetailAddedEvent(
                savedDetail.getId(),
                savedDetail.getInvoice().getId(),
                savedDetail.getLineTotal()
        ));
    }
}