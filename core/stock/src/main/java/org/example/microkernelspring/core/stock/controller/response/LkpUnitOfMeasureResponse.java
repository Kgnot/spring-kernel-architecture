package org.example.microkernelspring.core.stock.controller.response;

import java.util.UUID;

public record LkpUnitOfMeasureResponse(
        UUID id,
        String code,
        String name
) {}
