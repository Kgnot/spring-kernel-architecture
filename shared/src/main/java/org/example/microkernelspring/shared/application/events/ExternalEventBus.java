package org.example.microkernelspring.shared.application.events;

public interface ExternalEventBus {

    void publish(DomainEvent event);

}
