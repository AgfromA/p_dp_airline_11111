package ru.kata.airlineoauth.service.interfaces;

import org.springframework.stereotype.Component;
import ru.kata.airlineoauth.model.GoogleUser;

import java.security.Principal;

@Component
public interface GoogleUserParser {
    public GoogleUser parseUser(Principal principal);
}
