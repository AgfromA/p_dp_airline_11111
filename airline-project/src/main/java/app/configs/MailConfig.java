package app.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Конфигурационный класс, который создает бин JavaMailSender
 * (интерфейс в Spring Framework, который предоставляет функциональность для отправки электронной почты),
 * используемый в классе MailSender(@Link MailSender)
 */

@Configuration
public class MailConfig {

    /**
     * Данное поле привязывается к значению
     * хоста указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * Данное поле привязывается к значению
     * порта указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.port}")
    private int port;

    /**
     * Данное поле привязывается к значению
     * username(электронной почты) указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * Данное поле привязывается к значению
     * password(пароля) указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Данное поле привязывается к значению
     * условия smtp.auth(нужно ли аутентифицироваться для отправки электронной почты)
     * указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean smtpAuth;

    /**
     * Данное поле привязывается к значению
     * условия smtp.ssl(нужно ли шифровать соединение с помощью SSL)
     * указанного в application.yml(@Link application.yml)
     */
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private boolean smtpSsl;

    /**
     * Данный метод создает бин JavaMailSender-это интерфейс в Spring Framework,
     * который предоставляет функциональность для отправки электронной почты,
     * он будет в дальнейшем использоваться в классе MailSender(@Link MailSender).
     * JavaMailSender подключается к SMTP((Simple Mail Transfer Protocol — это протокол передачи почты),
     * который указан в проперти-файле application.yml(@Link application.yml)
     * @return бин JavaMailSender с заполненными полями,
     * которые были ранее указаны в application.yml(@Link application.yml), т.е. уже возвращается
     * авторизованный JavaMailSender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.ssl.enable", smtpSsl);
        props.put("mail.smtp.starttls.enable", smtpSsl);
        props.put("mail.debug", "true");

        return mailSender;
    }
}
