package org.example.microkernelspring.shared.application.events;

public interface DomainEventHandler<T extends DomainEvent> {

    String eventKey();

    void handle(T event);
}