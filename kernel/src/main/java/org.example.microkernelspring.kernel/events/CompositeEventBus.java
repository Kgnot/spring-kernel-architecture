package org.example.microkernelspring.kernel.events;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.example.microkernelspring.shared.application.events.ExternalEventBus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CompositeEventBus implements EventBus {

    private final InternalEventBus internalEventBus;
    private final ExternalEventBus externalEventBus;

    public CompositeEventBus(
            InternalEventBus internalEventBus,
            ExternalEventBus externalEventBus
    ) {
        this.internalEventBus = internalEventBus;
        this.externalEventBus = externalEventBus;
    }

    @Override
    public void publish(DomainEvent event) {
        switch (event.scope()) {
            case INTERNAL -> internalEventBus.publish(event);

            case EXTERNAL -> externalEventBus.publish(event);

            case BOTH -> {
                internalEventBus.publish(event);
                externalEventBus.publish(event);
            }
        }
    }
}