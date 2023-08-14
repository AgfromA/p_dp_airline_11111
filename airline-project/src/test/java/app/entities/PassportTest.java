package app.entities;

import app.enums.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


class PassportTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testRightPassport() {

        var passport1 = new Passport("Test", Gender.FEMALE, "3333 333333",
                LocalDate.of(2006, 3, 30), "Russia");
        Set<ConstraintViolation<Passport>> violations1 = validator.validate(passport1);
        Assertions.assertTrue(violations1.isEmpty());

        var passport2 = new Passport("Test1", Gender.MALE, "1233 567890",
                LocalDate.of(2006, 3, 30), "Russia");
        Set<ConstraintViolation<Passport>> violations2 = validator.validate(passport2);
        Assertions.assertTrue(violations2.isEmpty());
    }

    @Test
    public void testWrongPassport() {

        var passport1 = new Passport("T", Gender.FEMALE, "333L 333333",
                LocalDate.of(2006, 3, 30), "Russia");
        Set<ConstraintViolation<Passport>> violations1 = validator.validate(passport1);
        Assertions.assertFalse(violations1.isEmpty());

        var passport2 = new Passport("Test", Gender.MALE, "333L333333",
                LocalDate.of(2006, 3, 30), "Russia");
        Set<ConstraintViolation<Passport>> violations2 = validator.validate(passport2);
        Assertions.assertFalse(violations2.isEmpty());
    }

    @Test
    public void testWrongPassportJSON() {
        String passJson =
                "{" +
                        "\"middleName\": \"Test\"," +
                        "\"gender\": \"male\"," +
                        "\"serialNumberPassport\": \"3333 333333\"," +
                        "\"passportIssuingDate\": \"2006-03-30\"," +
                        "\"passportIssuingCountry\": \"Russia\"" +
                        "}";

        List<String> passportJsons = new ArrayList<>();

        passportJsons.add(
                "{" +
                        "\"middleName\": \"T\"," +
                        "\"gender\": \"male\"," +
                        "\"serialNumberPassport\": \"3333 333333\"," +
                        "\"passportIssuingDate\": \"2006-03-30\"," +
                        "\"passportIssuingCountry\": \"Russia\"" +
                        "}");

        passportJsons.add(
                "{" +
                        "\"middleName\": \"Test\"," +
                        "\"gender\": \"T\"," +
                        "\"serialNumberPassport\": \"3333 333333\"," +
                        "\"passportIssuingDate\": \"2006-03-30\"," +
                        "\"passportIssuingCountry\": \"Russia\"" +
                        "}");

        passportJsons.add(
                "{" +
                        "\"middleName\": \"Test\"," +
                        "\"gender\": \"male\"," +
                        "\"serialNumberPassport\": \"333L 333333\"," +
                        "\"passportIssuingDate\": \"2006-03-30\"," +
                        "\"passportIssuingCountry\": \"Russia\"" +
                        "}");

        passportJsons.add(
                "{" +
                        "\"middleName\": \"Test\"," +
                        "\"gender\": \"male\"," +
                        "\"serialNumberPassport\": \"333L 333333\"," +
                        "\"passportIssuingDate\": \"2006-15-30\"," +
                        "\"passportIssuingCountry\": \"Russia\"" +
                        "}");


        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Passport passport = null;
        List<Passport> wrongPassports = new ArrayList<>();
        try {
            passport = mapper.readValue(passJson, Passport.class);
            for (String json : passportJsons) {
                wrongPassports.add(mapper.readValue(json, Passport.class));
            }
        } catch (IOException ignored) {}

        Assertions.assertTrue(getSetOfViolation(validator, passport).isEmpty());
        wrongPassports.forEach(p -> Assertions.assertFalse(isSetWithViolationIsEmpty(validator, p)));
    }
}