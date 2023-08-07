package app.controllers.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Api(tags = "Main")
public interface ViewControllerApi {
    @ApiOperation(value = "Get view \"login\"")
    @GetMapping("/login")
    ModelAndView loginPage();
}
