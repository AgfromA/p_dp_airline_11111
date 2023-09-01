package app.controllers.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Hidden
@Api(tags = "Main")
@Tag(name = "Main", description = "Получение различных view")
public interface ViewControllerApi {

    @GetMapping("/")
    @ApiOperation(value = "Get view \"s7\"")
    ModelAndView index();

    @GetMapping("/searchResult")
    @ApiOperation(value = "Get view \"searchResult\"")
    ModelAndView searchResult();

    @ApiOperation(value = "Get view \"admin\"")
    @GetMapping("/admin")
    ModelAndView adminPage();

    @ApiOperation(value = "Get view \"manager\"")
    @GetMapping("/manager")
    ModelAndView managerPage();

    @ApiOperation(value = "Get view \"passenger\"")
    @GetMapping("/passenger")
    ModelAndView passengerPage();

    @ApiOperation(value = "Get view \"login\"")
    @GetMapping("/login")
    ModelAndView loginPage();
}