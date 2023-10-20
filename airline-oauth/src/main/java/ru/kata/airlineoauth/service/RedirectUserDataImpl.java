package ru.kata.airlineoauth.service;

import org.springframework.stereotype.Component;
import ru.kata.airlineoauth.model.GoogleUser;
import ru.kata.airlineoauth.service.interfaces.RedirectUserData;

import java.security.Principal;

@Component
public class RedirectUserDataImpl implements RedirectUserData {

    @Override
    public void sendUserDataToOtherService(GoogleUser googleUser, Principal principal) {
        // В этом методе отправляем данные на другой микросервис, например, через HTTP-запрос
        // Можно использовать RestTemplate или WebClient
    }
}
