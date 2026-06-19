package org.example.microkernelspring.kernel.notification;

public interface EmailGateway {
    void send(String destinatario, String asunto, String cuerpo);
}