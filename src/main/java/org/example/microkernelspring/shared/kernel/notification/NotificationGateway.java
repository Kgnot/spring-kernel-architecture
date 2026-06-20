package org.example.microkernelspring.shared.kernel.notification;

public interface NotificationGateway {
    void send(Long tenantId, String tipo, Object Payload);
}
