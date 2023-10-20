package ru.kata.airlineoauth.service;

import org.springframework.stereotype.Component;
import ru.kata.airlineoauth.model.GoogleUser;
import ru.kata.airlineoauth.service.interfaces.GoogleUserParser;

import java.security.Principal;

@Component
public class GoogleUserParserImpl implements GoogleUserParser {

    public GoogleUser parseUser(Principal principal) {

        String[] parts = principal.toString().split(",\\s+");
        String givenName = null;
        String familyName = null;
        String email = null;

        for (String part : parts) {
            if (part.startsWith("given_name=")) {
                givenName = part.substring("given_name=".length());
            } else if (part.startsWith("family_name=")) {
                familyName = part.substring("family_name=".length());
            } else if (part.startsWith("email=")) {
                email = part.substring("email=".length());
            }
        }

        GoogleUser googleUser = new GoogleUser(givenName,familyName,email);

        // Log4j добавить дебаг
//        System.out.println("Given Name: " + givenName);
//        System.out.println("Family Name: " + familyName);
//        System.out.println("Email: " + email);

        return googleUser;
    }
}
