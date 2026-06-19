package org.example.microkernelspring.kernel.websocket;

import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.kernel.notification.NotificationGateway;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class StompNotificationGateway implements NotificationGateway {

    private final SimpMessagingTemplate template;

    public StompNotificationGateway(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void send(Long tenantId, String tipo, Object payload) {
        log.info("Sending WS message to {}", "/topic/tenant/" + tenantId + "/notificaciones");

        template.convertAndSend("/topic/tenant/" + tenantId + "/notificaciones",
                Optional.of(Map.of("tipo", tipo, "data", payload)));
    }

}