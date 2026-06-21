package org.example.microkernelspring.kernel.events;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.DomainEventHandler;
import org.example.microkernelspring.shared.application.events.EventBus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InternalEventBus implements EventBus {

    private final Map<String, List<DomainEventHandler<?>>> handlersByKey;


    public InternalEventBus(List<DomainEventHandler<?>> handlers) {
        this.handlersByKey = handlers.stream()
                .collect(Collectors.groupingBy(DomainEventHandler::eventKey));

        log.info("InternalEventBus initialized with {} event keys", handlersByKey.size());
    }

    @Override
    public void publish(DomainEvent event) {

        var handlers = handlersByKey.getOrDefault(event.key(), List.of());
        log.info("Publishing {} event to {} handlers", event.key(), handlers.size());

        handlers.forEach(handler -> dispatch(handler, event));
    }

    @SuppressWarnings({"unchecket","rawtypes"})
    private void dispatch(DomainEventHandler handler, DomainEvent event) {
        try {
            handler.handle(event);
        } catch (Exception e) {
            log.error(
                    "Internal handler failed. eventKey={}, handler={}",
                    event.key(),
                    handler.getClass().getName(),
                    e
            );
            throw e;
        }
    }


}
