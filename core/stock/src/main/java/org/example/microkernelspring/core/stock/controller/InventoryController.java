package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.InventoryWebMapper;
import org.example.microkernelspring.core.stock.controller.request.CreateInventoryRequest;
import org.example.microkernelspring.core.stock.controller.response.InventoryResponse;
import org.example.microkernelspring.core.stock.service.InventoryService;
import org.example.microkernelspring.core.stock.usecase.CreateInventoryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final CreateInventoryUseCase createInventoryUseCase;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> findAll(@PathVariable UUID tenantId) {
        List<InventoryResponse> response = inventoryService.findAllByTenant(tenantId).stream()
                .map(InventoryWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> findById(
            @PathVariable UUID tenantId,
            @PathVariable UUID id
    ) {
        InventoryResponse response = InventoryWebMapper.toResponse(
                inventoryService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(
            @PathVariable UUID tenantId,
            @RequestBody CreateInventoryRequest request
    ) {
        InventoryResponse response = InventoryWebMapper.toResponse(
                createInventoryUseCase.execute(InventoryWebMapper.toServiceDto(request))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}