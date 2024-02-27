package app.controllers;

import app.controllers.api.EmailControllerApi;
import app.services.MailSender;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Это класс контроллера, который реализует отправку электронного письма, путем обращения к конечной точке,
 * определенной в интерфейсе EmailControllerApi(@Link EmailControllerApi)
 */
@Hidden
@RestController
@RequiredArgsConstructor
public class EmailController implements EmailControllerApi {
    /**
     * Определяется внедряемый экземпляр сервиса с логикой отправления электронного письма.
     * Внедрение происходит благодаря использованию аннотации RequiredArgsConstructor
     */
    private final MailSender mailSender;

    /**
     * Переопределяем метод из EmailControllerApi, с помощью которого будет осуществляться отправка электронного письма,
     * путем обращения к конечной точке
     * @param email данная переменная определяет электронную почту на которую будет отправлено электронное письмо
     * @return возвращаем HTTP-ответ в виде ResponseEntity
     */
    @Override
    public @ResponseBody ResponseEntity<String> sendEmail(String email) {
        try {
            mailSender.sendEmail(email, "Welcome", "This is a welcome email for your!!");
        } catch (MailException mailException) {
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
    }
}