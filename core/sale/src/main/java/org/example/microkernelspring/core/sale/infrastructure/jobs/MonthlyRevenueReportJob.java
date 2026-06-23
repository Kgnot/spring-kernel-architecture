package org.example.microkernelspring.core.sale.infrastructure.jobs;

import org.example.microkernelspring.core.sale.infrastructure.events.MonthlyRevenueReportGeneratedEvent;
import org.example.microkernelspring.core.sale.application.ports.TenantSalePort;
import org.example.microkernelspring.core.tenant.api.dto.EnableTenantDtoApi;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class MonthlyRevenueReportJob {

//    private final TenantSalePort port;
//    private final SalesRepository salesRepository;
//    private final EventBus eventBus;
//
//    public MonthlyRevenueReportJob(
//            TenantSalePort port,
//            SalesRepository salesRepository,
//            EventBus eventBus
//    ) {
//        this.port = port;
//        this.salesRepository = salesRepository;
//        this.eventBus = eventBus;
//    }
//
//    @Scheduled(cron = "0 0 8 1 * *")
//    public void sendReports() {
//        port
//                .getEnabledTenants("dashboard-sales")
//                .forEach(this::publishTenantReport);
//    }
//
//    private void publishTenantReport(EnableTenantDtoApi tenant) {
//        BigDecimal revenue = salesRepository.getPreviousMonthTotal(tenant.id());
//
//        MonthlyRevenueReportGeneratedEvent event =
//                new MonthlyRevenueReportGeneratedEvent(
//                        tenant.id(),
//                        "",//tenant.getEmail(),
//                        YearMonth.now().minusMonths(1),
//                        revenue
//                );
//
//        eventBus.publish(event);
//    }
}