package org.example.microkernelspring.shared.application.events;

public interface EventBus {

    void publish(DomainEvent event);
}