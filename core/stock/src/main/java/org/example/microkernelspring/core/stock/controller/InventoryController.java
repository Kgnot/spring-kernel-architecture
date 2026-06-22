package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.InventoryWebMapper;
import org.example.microkernelspring.core.stock.controller.request.CreateInventoryRequest;
import org.example.microkernelspring.core.stock.controller.response.InventoryResponse;
import org.example.microkernelspring.core.stock.service.InventoryService;
import org.example.microkernelspring.core.stock.usecase.CreateInventoryUseCase;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final CreateInventoryUseCase createInventoryUseCase;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> findAll() {
        // Sacamos el token por el contexto de seguridad
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        // Se lo pasamos al servicio
        List<InventoryResponse> response = inventoryService.findAllByTenant(tenantId).stream()
                .map(InventoryWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> findById(@PathVariable UUID id) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        InventoryResponse response = InventoryWebMapper.toResponse(
                inventoryService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(@RequestBody CreateInventoryRequest request) {
        // de aqui sacamos el tenant
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();
        var serviceDto = InventoryWebMapper.toServiceDto(request, tenantId);

        InventoryResponse response = InventoryWebMapper.toResponse(
                createInventoryUseCase.execute(serviceDto)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}