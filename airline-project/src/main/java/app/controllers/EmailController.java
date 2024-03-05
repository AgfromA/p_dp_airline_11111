package app.controllers;

import app.clients.MailClient;
import app.controllers.api.EmailControllerApi;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class EmailController implements EmailControllerApi {

    private final MailClient mailSender;

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