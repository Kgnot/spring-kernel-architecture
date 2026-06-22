package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.Inventory;
import org.example.microkernelspring.core.stock.entity.InventoryMovement;
import org.example.microkernelspring.core.stock.entity.LkpMovementType;
import org.example.microkernelspring.core.stock.entity.Supplier;
import org.example.microkernelspring.core.stock.repository.InventoryMovementRepository;
import org.example.microkernelspring.core.stock.repository.InventoryRepository;
import org.example.microkernelspring.core.stock.repository.LkpMovementTypeRepository;
import org.example.microkernelspring.core.stock.repository.SupplierRepository;
import org.example.microkernelspring.core.stock.service.dto.InventoryMovementDto;
import org.example.microkernelspring.core.stock.service.mapper.InventoryMovementMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterInventoryMovementUseCase {

    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryRepository inventoryRepository;
    private final LkpMovementTypeRepository movementTypeRepository;
    private final SupplierRepository supplierRepository;

    @Transactional
    public InventoryMovementDto execute(InventoryMovementDto dto) {
        // obtenemos el id del tenant
        UUID tenantId = dto.tenantId();
        // obtenemos el inventario por tenantId y movementId
        Inventory inventory = inventoryRepository
                .findByIdAndTenantId(dto.inventoryId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Inventory not found or access denied"));
        // buscamos el tipo de movimiento (aunque podría ir directamente del front)
        LkpMovementType movementType = movementTypeRepository
                .findById(dto.movementTypeId())
                .orElseThrow(() -> new RuntimeException("Movement type not found"));
        // buscamos el supplier | proveedor
        Supplier supplier = findSupplier(dto.supplierId(), tenantId);
        //actualizamos el balance
        updateInventoryBalance(inventory, movementType, dto.quantity());
        // guardamos el inventario
        inventoryRepository.save(inventory);
        // añadimos el movimiento
        InventoryMovement movement = InventoryMovementMapper.toEntity(
                dto,
                inventory,
                movementType,
                supplier
        );
        // registramos el movimiento
        InventoryMovement savedMovement = inventoryMovementRepository.save(movement);
        // devolvemos
        return InventoryMovementMapper.toDto(savedMovement);
    }

    private Supplier findSupplier(UUID supplierId, UUID tenantId) {
        if (supplierId == null) {
            return null;
        }

        return supplierRepository.findByIdAndTenantId(supplierId, tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found or access denied"));
    }

    private void updateInventoryBalance(
            Inventory inventory,
            LkpMovementType movementType,
            BigDecimal movementQuantity
    ) {
        BigDecimal currentQuantity = inventory.getQuantityOnHand();

        if (movementType.getDirection() == 1) {
            inventory.setQuantityOnHand(currentQuantity.add(movementQuantity));
            return;
        }

        if (movementType.getDirection() == -1) {
            if (currentQuantity.compareTo(movementQuantity) < 0) {
                throw new RuntimeException("Insufficient stock");
            }

            inventory.setQuantityOnHand(currentQuantity.subtract(movementQuantity));
            return;
        }

        throw new RuntimeException("Invalid movement direction");
    }
}