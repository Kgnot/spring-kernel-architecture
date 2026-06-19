package org.example.microkernelspring.jobs;

import org.example.microkernelspring.kernel.notification.EmailGateway;
import org.example.microkernelspring.service.TenantPluginService;
import org.example.microkernelspring.persistence.entity.Tenant;
import org.example.microkernelspring.persistence.repository.SalesRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MonthlyRevenueReportJob {

    private final TenantPluginService tenantPluginService;
    private final SalesRepository salesRepository;
    private final EmailGateway emailGateway;

    public MonthlyRevenueReportJob(
            TenantPluginService tenantPluginService,
            SalesRepository salesRepository,
            EmailGateway emailGateway
    ) {
        this.tenantPluginService = tenantPluginService;
        this.salesRepository = salesRepository;
        this.emailGateway = emailGateway;
    }

    @Scheduled(cron = "0 0 8 1 * *")
    public void sendReports() {

        tenantPluginService
                .getEnabledTenants("dashboard-sales")
                .forEach(this::sendTenantReport);
    }

    private void sendTenantReport(Tenant tenant) {

        BigDecimal revenue =
                salesRepository.getPreviousMonthTotal(
                        tenant.getId()
                );

        emailGateway.send(
                tenant.getEmail(),
                "Monthly Revenue Report",
                "Your revenue last month was: " + revenue
        );
    }
}