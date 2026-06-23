package org.example.microkernelspring.core.sale.api;

import org.example.microkernelspring.core.sale.api.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleApi {
    // Lecturas
    Optional<CustomerDtoApi> getCustomerById(UUID customerId);
    List<LkpDtoApi> getCustomerTypes();
    List<LkpDtoApi> getInvoiceStatuses();
    List<LkpDtoApi> getPaymentMethods();
    List<InvoiceDetailDtoApi> getInvoiceDetails(UUID invoiceId);

    // Escrituras
    void createCustomer(CreateCustomerDtoApi customer);
    void createInvoice(CreateInvoiceDtoApi invoice);
    void addInvoiceDetail(AddInvoiceDetailDtoApi invoiceDetail);
}