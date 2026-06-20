package org.example.microkernelspring.core.sale.service;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.core.sale.service.dto.CreateSaleDto;
import org.example.microkernelspring.shared.events.SaleCreatedEvent;
import org.example.microkernelspring.shared.kernel.KernelContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleService {

    private final KernelContext kernelContext;

    public SaleService(KernelContext kernelContext) {
        this.kernelContext = kernelContext;
    }

    public void createSale(
            Long tenantId,
            CreateSaleDto request
    ) {
        // guardamos en base de datos

        SaleCreatedEvent event = new SaleCreatedEvent(tenantId, request.amount());
        log.info("SaleCreatedEvent received");
        kernelContext.publish("venta.creada", event);
    }
}