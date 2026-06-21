package org.example.microkernelspring.plugins.dashboardVentasPlugin;


import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.shared.application.events.SaleCreatedEvent;
import org.example.microkernelspring.shared.application.kernel.KernelContext;
import org.example.microkernelspring.shared.application.kernel.Plugin;
import org.example.microkernelspring.shared.application.kernel.PluginType;
import org.example.microkernelspring.websocket.NotificationGateway;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DashboardVentasPlugin implements Plugin {

    private final KernelContext ctx;

    public DashboardVentasPlugin(KernelContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public String getId() {
        return "dashboard-sales";
    }

    @Override
    public PluginType getType() {
        return PluginType.UI;
    }

    @Override
    public void onLoad(KernelContext ctx) {

        log.info("DashboardVentasPlugin loaded");

        ctx.subscribe("venta.creada", this::recalculateKPIs);
    }

    @Override
    public void onUnload(KernelContext context) {
        log.info("DashboardVentasPlugin unloaded");
    }

    private void recalculateKPIs(String event, Object data) {
        log.info("Dashboard plugin received event {}", event);
        SaleCreatedEvent sale = (SaleCreatedEvent) data;
        log.info("Sending notification to tenant {} amount {}", sale.tenantId(), sale.amount());

        ctx.getService(NotificationGateway.class)
                .send(sale.tenantId(), "kpi-updated", sale.amount());
    }
}