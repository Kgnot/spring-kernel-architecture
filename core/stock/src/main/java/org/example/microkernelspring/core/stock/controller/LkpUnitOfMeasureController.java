package org.example.microkernelspring.core.stock.controller;


import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.LkpUnitOfMeasureWebMapper;
import org.example.microkernelspring.core.stock.controller.response.LkpUnitOfMeasureResponse;
import org.example.microkernelspring.core.stock.service.LkpUnitOfMeasureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lkp-units-of-measure")
@RequiredArgsConstructor
public class LkpUnitOfMeasureController {

    private final LkpUnitOfMeasureService lkpUnitOfMeasureService;

    @GetMapping
    public ResponseEntity<List<LkpUnitOfMeasureResponse>> findAll() {
        List<LkpUnitOfMeasureResponse> response = lkpUnitOfMeasureService.findAll().stream()
                .map(LkpUnitOfMeasureWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LkpUnitOfMeasureResponse> findById(
            @PathVariable UUID id
    ) {
        LkpUnitOfMeasureResponse response = LkpUnitOfMeasureWebMapper.toResponse(
                lkpUnitOfMeasureService.findById(id)
        );

        return ResponseEntity.ok(response);
    }
}