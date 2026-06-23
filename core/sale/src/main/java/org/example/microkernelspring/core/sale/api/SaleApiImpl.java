package org.example.microkernelspring.core.sale.api;

import org.example.microkernelspring.core.sale.api.dto.*;
import org.example.microkernelspring.core.sale.application.services.*;
import org.example.microkernelspring.core.sale.application.usecase.AddInvoiceDetailUseCase;
import org.example.microkernelspring.core.sale.application.usecase.CreateCustomerUseCase;
import org.example.microkernelspring.core.sale.application.usecase.CreateInvoiceUseCase;
import org.example.microkernelspring.core.sale.application.usecase.command.AddInvoiceDetailCommand;
import org.example.microkernelspring.core.sale.application.usecase.command.CreateCustomerCommand;
import org.example.microkernelspring.core.sale.application.usecase.command.CreateInvoiceCommand;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleApiImpl implements SaleApi {

    private final CustomerQueryService customerQueryService;
    private final InvoiceDetailsQueryService invoiceDetailsQueryService;
    private final LkpCustomerTypeQueryService lkpCustomerTypeQueryService;
    private final LkpInvoiceStatusQueryService lkpInvoiceStatusQueryService;
    private final LkpPaymentMethodQueryService lkpPaymentMethodQueryService;

    private final CreateCustomerUseCase createCustomerUseCase;
    private final CreateInvoiceUseCase createInvoiceUseCase;
    private final AddInvoiceDetailUseCase addInvoiceDetailUseCase;


    @Override
    public Optional<CustomerDtoApi> getCustomerById(UUID customerId) {
        var result = customerQueryService.findById(customerId);
        return Optional.of(new CustomerDtoApi(result.getId(), result.getTenantId(), result.getProfileId(), result.getCompanyName(), result.getTaxId(), result.getCreditLimit()));
    }

    @Override
    public List<LkpDtoApi> getCustomerTypes() {
        return lkpCustomerTypeQueryService.findAll().stream()
                .map(dto -> new LkpDtoApi(dto.getId(), dto.getCode(), dto.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LkpDtoApi> getInvoiceStatuses() {
        return lkpInvoiceStatusQueryService.findAll().stream()
                .map(dto -> new LkpDtoApi(dto.getId(), dto.getCode(), dto.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LkpDtoApi> getPaymentMethods() {
        return lkpPaymentMethodQueryService.findAll().stream()
                .map(dto -> new LkpDtoApi(dto.getId(), dto.getCode(), dto.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDetailDtoApi> getInvoiceDetails(UUID invoiceId) {
        return invoiceDetailsQueryService.findByInvoiceId(invoiceId,null)
                .stream()
                .map(dto -> new InvoiceDetailDtoApi(dto.getId(), dto.getInvoiceId(), dto.getProductId(), dto.getServiceId(), dto.getDescription(), dto.getQuantity(), dto.getUnitPrice(), dto.getDiscount(), dto.getTaxRate(), dto.getLineTotal()))
                .collect(Collectors.toList());
    }

    @Override
    public void createCustomer(CreateCustomerDtoApi dto) {
        CreateCustomerCommand command = new CreateCustomerCommand(
                dto.tenantId(), dto.profileId(), dto.customerTypeId(), dto.companyName(), dto.taxId(), dto.creditLimit()
        );
        createCustomerUseCase.execute(command);
    }

    @Override
    public void createInvoice(CreateInvoiceDtoApi dto) {
        CreateInvoiceCommand command = new CreateInvoiceCommand(
                dto.tenantId(), dto.siteId(), dto.customerId(), dto.employeeId(), dto.invoiceNumber(),
                dto.statusId(), dto.subtotal(), dto.taxTotal(), dto.discountTotal(), dto.grandTotal(),
                dto.currency(), dto.issuedAt(), dto.dueAt()
        );
        createInvoiceUseCase.execute(command);
    }

    @Override
    public void addInvoiceDetail(AddInvoiceDetailDtoApi dto) {
        AddInvoiceDetailCommand command = new AddInvoiceDetailCommand(
                dto.invoiceId(), dto.productId(), dto.serviceId(), dto.description(),
                dto.quantity(), dto.unitPrice(), dto.discount(), dto.taxRate(), dto.lineTotal()
        );
        addInvoiceDetailUseCase.execute(command);
    }
}