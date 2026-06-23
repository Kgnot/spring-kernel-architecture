package org.example.microkernelspring.core.sale.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SaleApi {

    // Lecturas
    List<CustomerApi> getAllCustomers();
    CustomerApi getCustomerById(UUID id);

    // Escrituras
    void createCustomer(CustomerApi customer);
    void createInvoice(InvoiceApi invoice);

    // DTOs API
    record CustomerApi(UUID id, UUID tenantId, UUID profileId, String companyName, String taxId, BigDecimal creditLimit) {}
    record InvoiceApi(UUID id, UUID tenantId, UUID siteId, UUID customerId, UUID employeeId, String invoiceNumber, UUID statusId, BigDecimal subtotal, BigDecimal taxTotal, BigDecimal discountTotal, BigDecimal grandTotal, String currency, Instant issuedAt, Instant dueAt) {}
}