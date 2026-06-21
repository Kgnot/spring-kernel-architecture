package org.example.microkernelspring.websocket;

public interface NotificationGateway {
    void send(Long tenantId, String tipo, Object Payload);
}
