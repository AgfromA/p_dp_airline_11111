package app.controllers.rest;

import app.controllers.api.ViewControllerApi;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class ViewController implements ViewControllerApi {
    @Override
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }
}
