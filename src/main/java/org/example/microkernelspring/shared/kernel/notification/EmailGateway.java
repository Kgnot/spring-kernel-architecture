package org.example.microkernelspring.shared.kernel.notification;

public interface EmailGateway {
    void send(String destinatario, String asunto, String cuerpo);
}