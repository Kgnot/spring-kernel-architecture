package org.example.microkernelspring.kernel.notification;

public interface NotificationGateway {
    void send(Long tenantId, String tipo, Object Payload);
}
