package app.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Клиент для отправки писем пользователям
 * Использует библиотеку Spring mail
 */
@Service
@RequiredArgsConstructor
public class MailClient {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendEmail(String emailTo, String subject, String emailText) {
        SimpleMailMessage message = new SimpleMailMessage();

        // FIXME кажется, что можно обойтись без этого, так как это стандартнаая проперти спринга,
        //  и она должна уметь инжектиться самостоятельно. Проверить
        message.setFrom(emailFrom);

        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(emailText);
        mailSender.send(message);
    }
}