package org.example.microkernelspring.core.stock.controller;


import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.InventoryMovementWebMapper;
import org.example.microkernelspring.core.stock.controller.request.RegisterInventoryMovementRequest;
import org.example.microkernelspring.core.stock.controller.response.InventoryMovementResponse;
import org.example.microkernelspring.core.stock.service.InventoryMovementService;
import org.example.microkernelspring.core.stock.usecase.RegisterInventoryMovementUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/inventory-movements")
@RequiredArgsConstructor
public class InventoryMovementController {

    private final InventoryMovementService inventoryMovementService;
    private final RegisterInventoryMovementUseCase registerInventoryMovementUseCase;

    @GetMapping
    public ResponseEntity<List<InventoryMovementResponse>> findAll(@PathVariable UUID tenantId) {
        List<InventoryMovementResponse> response = inventoryMovementService.findAllByTenant(tenantId).stream()
                .map(InventoryMovementWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementResponse> findById(
            @PathVariable UUID tenantId,
            @PathVariable UUID id
    ) {
        InventoryMovementResponse response = InventoryMovementWebMapper.toResponse(
                inventoryMovementService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InventoryMovementResponse> register(
            @PathVariable UUID tenantId,
            @RequestBody RegisterInventoryMovementRequest request
    ) {
        InventoryMovementResponse response = InventoryMovementWebMapper.toResponse(
                registerInventoryMovementUseCase.execute(InventoryMovementWebMapper.toServiceDto(request))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}