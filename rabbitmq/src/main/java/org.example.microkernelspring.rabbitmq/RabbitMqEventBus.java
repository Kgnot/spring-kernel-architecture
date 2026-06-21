package org.example.microkernelspring.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.ExternalEventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqEventBus implements ExternalEventBus {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public RabbitMqEventBus(
            RabbitTemplate rabbitTemplate,
            @Value("${app.events.rabbit.exchange:domain.events}") String exchange
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @Override
    public void publish(DomainEvent event) {
        log.info(
                "Publishing RabbitMQ event exchange={}, routingKey={}",
                exchange,
                event.key()
        );

        rabbitTemplate.convertAndSend(
                exchange,
                event.key(),
                event
        );
    }
}