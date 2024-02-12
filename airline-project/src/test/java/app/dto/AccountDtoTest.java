package app.dto;


import app.entities.EntityTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

class AccountDtoTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;
    private AccountDto accountDTO;
    private JSONObject accountJsonObject;

    @BeforeEach
    public void setUp() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        accountDTO = new AccountDto();
        accountJsonObject = initValidableJSONObject();
    }

    private JSONObject initValidableJSONObject() {
        var validableAccountJson = new JSONObject();
        validableAccountJson.put("id", "1002");
        validableAccountJson.put("firstName", "Olga");
        validableAccountJson.put("lastName", "Alikulieva");
        validableAccountJson.put("birthDate", "1998-01-08");
        validableAccountJson.put("email", "account@mail.ru");
        validableAccountJson.put("phoneNumber", "79997779999");
        validableAccountJson.put("password", "1@Password");
        validableAccountJson.put("securityQuestion", "securityQuestion");
        validableAccountJson.put("answerQuestion", "answerQuestion");
        validableAccountJson.put("roles", null);
        return validableAccountJson;
    }

    @Test
    void validAccountShouldValidate() {
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "a, false", // short first name
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, false", // long first name
            "1#qwe, false", // contains characters other than letters
    })
    void firstNameValidationTest(String firstName, boolean expectedResult) {
        accountJsonObject.replace("firstName", firstName);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "a, false", // short last name
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, false", // long last name
            "1#qwe, false", // contains characters other than letters
    })
    void lastNameValidationTest(String lastName, boolean expectedResult) {
        accountJsonObject.replace("lastName", lastName);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "1990-05-15, true", // correct date
            "2025-01-01, false", // date in the future
    })
    void birthdayValidationTest(String birthDate, boolean expectedResult) {
        accountJsonObject.replace("birthDate", birthDate);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "test@example.com, true", // корректный email
            "testexample.com, false", // отсутствует символ @
            "test@example, false", // отсутствует домен
            "@example.com, false", // отсутствует часть перед @
            "test@example.com., false", // точка в конце доменного имени
            "test@example_com, false", // недопустимый символ в домене
            "test@.com, false", // отсутствует домен
            "test#@example_com, false", // недопустимый символ
    })
    void emailValidationTest(String email, boolean expectedResult) {
        accountJsonObject.replace("email", email);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "12345678, true", // correct number
            "1111111111111111111111111111111111111111111111111111111111111" +
            "22222222222222222222222222222222, false", // long phone number

    })
    void phoneNumberValidationTest(String phoneNumber, boolean expectedResult) {
        accountJsonObject.replace("phoneNumber", phoneNumber);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @ParameterizedTest
    @CsvSource({
            ", false", // empty field
            "Password1@, true", // корректный пароль
            "Test@1234, true", // корректный пароль
            "Test@1234, true", // корректный пароль
            "pass, false", // менее 8 символов
            "Password, false", // отсутствует специальный символ
            "12345678, false", // отсутствуют буквы и специальные символы
    })
    void passwordValidationTest(String password, boolean expectedResult) {
        accountJsonObject.replace("password", password);
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expectedResult, isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    void blankQuestionShouldNotValidate() {
        accountJsonObject.replace("securityQuestion", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    void blankAnswerShouldNotValidate() {
        accountJsonObject.replace("answerQuestion", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }
}