package ru.kata.airlineoauth.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.kata.airlineoauth.model.GoogleUser;
import ru.kata.airlineoauth.service.interfaces.GoogleUserParser;
import ru.kata.airlineoauth.service.interfaces.RedirectUserData;

import java.security.Principal;

@Controller
public class RedirectController {

    private final GoogleUserParser googleUserParser;
    private final RedirectUserData redirectUserData;

    @Autowired
    public RedirectController(GoogleUserParser googleUserParser, RedirectUserData redirectUserData) {
        this.googleUserParser = googleUserParser;
        this.redirectUserData = redirectUserData;
    }

    @GetMapping("/")
    public RedirectView redirectToAnotherPort(Principal principal) {

        GoogleUser googleUser = googleUserParser.parseUser(principal);
        redirectUserData.sendUserDataToOtherService(googleUser, principal);

        return new RedirectView("http://localhost:8082/admin");
    }



}
