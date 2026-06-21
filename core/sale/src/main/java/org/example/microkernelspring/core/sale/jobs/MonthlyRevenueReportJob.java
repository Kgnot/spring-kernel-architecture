package org.example.microkernelspring.core.sale.jobs;

import org.example.microkernelspring.core.sale.events.MonthlyRevenueReportGeneratedEvent;
import org.example.microkernelspring.core.sale.persistence.repository.SalesRepository;
import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.example.microkernelspring.core.tenant.service.TenantPluginService;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class MonthlyRevenueReportJob {

    private final TenantPluginService tenantPluginService;
    private final SalesRepository salesRepository;
    private final EventBus eventBus;

    public MonthlyRevenueReportJob(
            TenantPluginService tenantPluginService,
            SalesRepository salesRepository,
            EventBus eventBus
    ) {
        this.tenantPluginService = tenantPluginService;
        this.salesRepository = salesRepository;
        this.eventBus = eventBus;
    }

    @Scheduled(cron = "0 0 8 1 * *")
    public void sendReports() {
        tenantPluginService
                .getEnabledTenants("dashboard-sales")
                .forEach(this::publishTenantReport);
    }

    private void publishTenantReport(Tenant tenant) {
        BigDecimal revenue = salesRepository.getPreviousMonthTotal(tenant.getId());

        MonthlyRevenueReportGeneratedEvent event =
                new MonthlyRevenueReportGeneratedEvent(
                        tenant.getId(),
                        "",//tenant.getEmail(),
                        YearMonth.now().minusMonths(1),
                        revenue
                );

        eventBus.publish(event);
    }
}