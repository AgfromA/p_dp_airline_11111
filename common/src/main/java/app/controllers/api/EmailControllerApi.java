package app.controllers.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * В данном интерфейсе содержится метод,
 * реализуемый в дальнейшем в контроллере EmailController (@Link EmailController),
 * и определяются конечные точки
 */
@Hidden
@RequestMapping("/email")
public interface EmailControllerApi {

    @GetMapping(value = "/simple-email/{userEmail}")
    @ResponseBody
    ResponseEntity<String> sendEmail(@PathVariable("userEmail") String email);
}