package org.example.microkernelspring.notification;

public interface EmailGateway {
    void send(String destinatario, String asunto, String cuerpo);
}