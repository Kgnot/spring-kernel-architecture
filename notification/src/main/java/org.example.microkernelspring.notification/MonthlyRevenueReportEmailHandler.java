package org.example.microkernelspring.notification;

import org.example.microkernelspring.core.sale.events.MonthlyRevenueReportGeneratedEvent;
import org.example.microkernelspring.shared.application.events.DomainEventHandler;
import org.springframework.stereotype.Component;

@Component
public class MonthlyRevenueReportEmailHandler
        implements DomainEventHandler<MonthlyRevenueReportGeneratedEvent> {

    private final EmailGateway emailGateway;

    public MonthlyRevenueReportEmailHandler(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    @Override
    public String eventKey() {
        return MonthlyRevenueReportGeneratedEvent.KEY;
    }

    @Override
    public void handle(MonthlyRevenueReportGeneratedEvent event) {
        emailGateway.send(
                event.recipientEmail(),
                "Monthly Revenue Report",
                "Your revenue for " + event.period()
                        + " was: " + event.revenue()
        );
    }
}