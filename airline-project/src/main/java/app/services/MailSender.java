package app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/**
 * Данный класс является сервисом,
 * который определяет логику метода отправки электронного письма
 */
@Service
@RequiredArgsConstructor
public class MailSender {

    /**
     * Внедряем бин JavaMailSender,
     * определенный в классе MailConfig(@Link MailConfig),
     * с помощью аннотации RequiredArgsConstructor
     */
    private final JavaMailSender mailSender;

    /**
     * Данное поле привязывается к значению
     * username указанного в application.yml(@Link application.yml)
     * и будет использовано при указании отправителя электронного письма
     */
    @Value("${spring.mail.username}")
    private String emailFrom;

    /**
     * Данный метод определяет логику отправления электронного письма
     * @param to этот параметр определяет адресата письма
     * @param subject этот параметр определяет тему письма
     * @param text этот параметр определяет текст письма
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
