package org.example.microkernelspring.shared.kernel.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailGateway implements EmailGateway {

    private final JavaMailSender mailSender;

    public SmtpEmailGateway(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(
            String destinatario,
            String asunto,
            String cuerpo
    ) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(destinatario);
        msg.setSubject(asunto);
        msg.setText(cuerpo);

        mailSender.send(msg);
    }
}