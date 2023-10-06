package ru.kata.airlineoauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class GoogleUser {
    private String givenName;
    private String familyName;
    private String email;
}
