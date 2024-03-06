package app.controllers.api;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Hidden
@Api(tags = "Email API")
@Tag(name = "Email API", description = "API для проверки работоспособности отправки писем юзерам. Не относится к основной логике приложения")
@RequestMapping("/email")
public interface EmailControllerApi {

    @GetMapping(value = "/{userEmail}")
    @ResponseBody
    ResponseEntity<String> sendEmail(@PathVariable("userEmail") String email);
}