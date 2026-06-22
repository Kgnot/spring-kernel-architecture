package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.SupplierWebMapper;
import org.example.microkernelspring.core.stock.controller.request.CreateSupplierRequest;
import org.example.microkernelspring.core.stock.controller.response.SupplierResponse;
import org.example.microkernelspring.core.stock.service.SupplierService;
import org.example.microkernelspring.core.stock.usecase.CreateSupplierUseCase;
import org.example.microkernelspring.core.stock.usecase.DeleteSupplierUseCase;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final CreateSupplierUseCase createSupplierUseCase;
    private final DeleteSupplierUseCase deleteSupplierUseCase;

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> findAll() {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        List<SupplierResponse> response = supplierService.findAllByTenant(tenantId).stream()
                .map(SupplierWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> findById(@PathVariable UUID id) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        SupplierResponse response = SupplierWebMapper.toResponse(
                supplierService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> create(@RequestBody CreateSupplierRequest request) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        SupplierResponse response = SupplierWebMapper.toResponse(
                createSupplierUseCase.execute(SupplierWebMapper.toServiceDto(request, tenantId))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        deleteSupplierUseCase.execute(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}