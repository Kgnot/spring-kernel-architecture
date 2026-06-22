package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.LkpMovementTypeWebMapper;
import org.example.microkernelspring.core.stock.controller.response.LkpMovementTypeResponse;
import org.example.microkernelspring.core.stock.service.LkpMovementTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lkp-movement-types")
@RequiredArgsConstructor
public class LkpMovementTypeController {

    private final LkpMovementTypeService lkpMovementTypeService;

    @GetMapping
    public ResponseEntity<List<LkpMovementTypeResponse>> findAll() {
        List<LkpMovementTypeResponse> response = lkpMovementTypeService.findAll().stream()
                .map(LkpMovementTypeWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LkpMovementTypeResponse> findById(@PathVariable UUID id) {
        LkpMovementTypeResponse response = LkpMovementTypeWebMapper.toResponse(
                lkpMovementTypeService.findById(id)
        );

        return ResponseEntity.ok(response);
    }
}