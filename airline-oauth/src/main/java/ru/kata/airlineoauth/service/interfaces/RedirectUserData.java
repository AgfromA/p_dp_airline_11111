package ru.kata.airlineoauth.service.interfaces;

import org.springframework.stereotype.Component;
import ru.kata.airlineoauth.model.GoogleUser;

import java.security.Principal;

@Component
public interface RedirectUserData {
    public void sendUserDataToOtherService(GoogleUser googleUser, Principal principal);
}
