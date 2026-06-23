package org.example.microkernelspring.core.sale.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.services.InvoiceDetailsQueryService;
import org.example.microkernelspring.core.sale.application.services.InvoiceDetailsQueryService.InvoiceDetailDto;
import org.example.microkernelspring.core.sale.application.usecase.AddInvoiceDetailUseCase;
import org.example.microkernelspring.core.sale.application.usecase.command.AddInvoiceDetailCommand;
import org.example.microkernelspring.core.sale.infrastructure.controllers.request.AddInvoiceDetailRequest;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.InvoiceDetailResponse;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices/{invoiceId}/details")
@RequiredArgsConstructor
public class InvoiceDetailsController {

    private final InvoiceDetailsQueryService invoiceDetailsQueryService;
    private final AddInvoiceDetailUseCase addInvoiceDetailUseCase;

    /**
     * GET /api/invoices/{invoiceId}/details?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<PageResponse<InvoiceDetailResponse>> getAll(
            @PathVariable UUID invoiceId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("desc") ? Sort.by("id").descending() : Sort.by("id").ascending());

        var resultPage = invoiceDetailsQueryService.findByInvoiceId(invoiceId, pageable);
        return ResponseEntity.ok(PageResponse.of(resultPage.map(this::toResponse)));
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable UUID invoiceId,
            @RequestBody AddInvoiceDetailRequest request
    ) {
        addInvoiceDetailUseCase.execute(new AddInvoiceDetailCommand(
                invoiceId,
                request.productId(), request.serviceId(), request.description(),
                request.quantity(), request.unitPrice(), request.discount(),
                request.taxRate(), request.lineTotal()
        ));
        return ResponseEntity.status(201).build();
    }

    private InvoiceDetailResponse toResponse(InvoiceDetailDto dto) {
        return new InvoiceDetailResponse(
                dto.getId(), dto.getInvoiceId(), dto.getProductId(), dto.getServiceId(),
                dto.getDescription(), dto.getQuantity(), dto.getUnitPrice(),
                dto.getDiscount(), dto.getTaxRate(), dto.getLineTotal()
        );
    }
}
