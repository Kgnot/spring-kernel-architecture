package org.example.microkernelspring.core.sale.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.services.CustomerQueryService;
import org.example.microkernelspring.core.sale.application.services.CustomerQueryService.CustomerDto;
import org.example.microkernelspring.core.sale.application.usecase.CreateCustomerUseCase;
import org.example.microkernelspring.core.sale.application.usecase.command.CreateCustomerCommand;
import org.example.microkernelspring.core.sale.infrastructure.controllers.request.CreateCustomerRequest;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.CustomerResponse;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.PageResponse;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerQueryService customerQueryService;
    private final CreateCustomerUseCase createCustomerUseCase;


    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size, sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        var tenantId = SecurityContextHelper.getCurrentTenantId();

        var resultPage = customerQueryService.findByTenantId(tenantId, pageable);


        return ResponseEntity.ok(PageResponse.of(resultPage.map(this::toResponse)));
    }

    /** GET /api/customers/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(customerQueryService.findById(id)));
    }

    /** POST /api/customers */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateCustomerRequest request) {
        var tenantId = SecurityContextHelper.getCurrentTenantId();
        createCustomerUseCase.execute(new CreateCustomerCommand(
                tenantId,
                request.profileId(),
                request.customerTypeId(),
                request.companyName(),
                request.taxId(),
                request.creditLimit()
        ));
        return ResponseEntity.status(201).build();
    }

    private CustomerResponse toResponse(CustomerDto dto) {
        return new CustomerResponse(
                dto.getId(), dto.getTenantId(), dto.getProfileId(),
                dto.getCompanyName(), dto.getTaxId(), dto.getCreditLimit()
        );
    }
}
