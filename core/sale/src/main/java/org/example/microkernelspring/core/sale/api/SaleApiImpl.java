package org.example.microkernelspring.core.sale.api;

import org.example.microkernelspring.core.sale.services.CustomerQueryService;
import org.example.microkernelspring.core.sale.services.InvoiceQueryService;
import org.example.microkernelspring.core.sale.usecase.CreateCustomerUseCase;
import org.example.microkernelspring.core.sale.usecase.CreateInvoiceUseCase;
import org.example.microkernelspring.core.sale.usecase.command.CreateCustomerCommand;
import org.example.microkernelspring.core.sale.usecase.command.CreateInvoiceCommand;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleApiImpl implements SaleApi {

    private final CustomerQueryService customerQueryService;
    private final InvoiceQueryService invoiceQueryService;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final CreateInvoiceUseCase createInvoiceUseCase;

    @Override
    public List<CustomerApi> getAllCustomers() {
        return customerQueryService.findAll().stream()
                .map(dto -> new CustomerApi(dto.getId(), dto.getTenantId(), dto.getProfileId(), dto.getCompanyName(), dto.getTaxId(), dto.getCreditLimit()))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerApi getCustomerById(UUID id) {
        CustomerQueryService.CustomerDto dto = customerQueryService.findById(id);
        return new CustomerApi(dto.getId(), dto.getTenantId(), dto.getProfileId(), dto.getCompanyName(), dto.getTaxId(), dto.getCreditLimit());
    }

    @Override
    public void createCustomer(CustomerApi customer) {
        CreateCustomerCommand command = new CreateCustomerCommand(
                customer.tenantId(),
                customer.profileId(),
                null, // Asumiendo que customerTypeId se gestiona o se añade al ApiDTO si lo necesitas estrictamente
                customer.companyName(),
                customer.taxId(),
                customer.creditLimit()
        );
        createCustomerUseCase.execute(command);
    }

    @Override
    public void createInvoice(InvoiceApi invoice) {
        CreateInvoiceCommand command = new CreateInvoiceCommand(
                invoice.tenantId(),
                invoice.siteId(),
                invoice.customerId(),
                invoice.employeeId(),
                invoice.invoiceNumber(),
                invoice.statusId(),
                invoice.subtotal(),
                invoice.taxTotal(),
                invoice.discountTotal(),
                invoice.grandTotal(),
                invoice.currency(),
                invoice.issuedAt(),
                invoice.dueAt()
        );
        createInvoiceUseCase.execute(command);
    }
}