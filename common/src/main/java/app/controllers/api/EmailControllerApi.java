package app.controllers.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Hidden
@RequestMapping("/email")
public interface EmailControllerApi {

    @GetMapping(value = "/simple-email/{user-email}")
    @ResponseBody
    ResponseEntity<String> sendEmail(@PathVariable("user-email") String email);
}