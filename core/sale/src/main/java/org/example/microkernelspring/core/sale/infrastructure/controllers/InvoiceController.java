package org.example.microkernelspring.core.sale.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.services.InvoiceQueryService;
import org.example.microkernelspring.core.sale.application.services.InvoiceQueryService.InvoiceDto;
import org.example.microkernelspring.core.sale.application.usecase.CreateInvoiceUseCase;
import org.example.microkernelspring.core.sale.application.usecase.command.CreateInvoiceCommand;
import org.example.microkernelspring.core.sale.infrastructure.controllers.request.CreateInvoiceRequest;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.InvoiceResponse;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.PageResponse;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceQueryService invoiceQueryService;
    private final CreateInvoiceUseCase createInvoiceUseCase;

    /**
     * GET /api/invoices?page=0&size=20&sort=issuedAt,desc
     * Filtros opcionales: tenantId, customerId
     */
    @GetMapping
    public ResponseEntity<PageResponse<InvoiceResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "issuedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UUID customerId
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var tenantId = SecurityContextHelper.getCurrentTenantId();

        var resultPage = customerId != null
                ? invoiceQueryService.findByCustomerId(customerId, pageable)
                : invoiceQueryService.findByTenantId(tenantId, pageable);

        return ResponseEntity.ok(PageResponse.of(resultPage.map(this::toResponse)));
    }

    /**
     * GET /api/invoices/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(invoiceQueryService.findById(id)));
    }

    /**
     * POST /api/invoices
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateInvoiceRequest request) {
        var tenantId = SecurityContextHelper.getCurrentTenantId();

        createInvoiceUseCase.execute(new CreateInvoiceCommand(
                tenantId, request.siteId(), request.customerId(), request.employeeId(),
                request.invoiceNumber(), request.statusId(), request.subtotal(), request.taxTotal(),
                request.discountTotal(), request.grandTotal(), request.currency(),
                request.issuedAt(), request.dueAt()
        ));
        return ResponseEntity.status(201).build();
    }

    private InvoiceResponse toResponse(InvoiceDto dto) {
        return new InvoiceResponse(
                dto.getId(), dto.getTenantId(), dto.getSiteId(), dto.getCustomerId(),
                dto.getEmployeeId(), dto.getInvoiceNumber(), dto.getStatusId(),
                dto.getSubtotal(), dto.getTaxTotal(), dto.getDiscountTotal(),
                dto.getGrandTotal(), dto.getCurrency(), dto.getIssuedAt(), dto.getDueAt()
        );
    }
}
