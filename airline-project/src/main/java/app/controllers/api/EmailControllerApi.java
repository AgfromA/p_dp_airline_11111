package app.controllers.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Api(tags = "Email")
@Tag(name = "Email", description = "Отправка сообщений на email")
@RequestMapping("/email")
public interface EmailControllerApi {

    @GetMapping(value = "/simple-email/{user-email}")
    @ApiOperation(value = "Send email to User")
    @ResponseBody
    ResponseEntity<String> sendEmail(
            @ApiParam(
                    name = "email",
                    value = "User's email"
            )
            @PathVariable("user-email") String email);
}