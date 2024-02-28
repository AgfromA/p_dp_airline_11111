package app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 * Сервис, который определяет логику метода отправки электронного письма
 */
@Service
@RequiredArgsConstructor
public class MailSender {

    /**
     * Внедряем бин JavaMailSender(интерфейс в Spring Framework,
     * который предоставляет функциональность для отправки электронной почты),
     * определенный в классе MailConfig(@Link MailConfig).
     * JavaMailSender подключается к SMTP((Simple Mail Transfer Protocol — это протокол передачи почты),
     * который указан в проперти-файле application.yml(@Link application.yml).
     *
     */
    private final JavaMailSender mailSender;

    /**
     * Поле с username-ом(электронной почтой) отправителя письма
     */
    @Value("${spring.mail.username}")
    private String emailFrom;

    /**
     * @param emailTo этот параметр определяет адресата письма
     * @param subject этот параметр определяет тему письма
     * @param emailText этот параметр определяет текст письма
     */
    public void sendEmail(String emailTo, String subject, String emailText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(emailText);
        mailSender.send(message);
    }
}
