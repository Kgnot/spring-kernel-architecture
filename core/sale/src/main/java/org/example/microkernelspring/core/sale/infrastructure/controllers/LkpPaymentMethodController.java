package org.example.microkernelspring.core.sale.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.services.LkpPaymentMethodQueryService;
import org.example.microkernelspring.core.sale.infrastructure.controllers.response.LkpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class LkpPaymentMethodController {

    private final LkpPaymentMethodQueryService queryService;

    /** GET /api/payment-methods */
    @GetMapping
    public ResponseEntity<List<LkpResponse>> getAll() {
        List<LkpResponse> result = queryService.findAll().stream()
                .map(dto -> new LkpResponse(dto.getId(), dto.getCode(), dto.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
