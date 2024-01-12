package app.dto;


import app.entities.EntityTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class AccountDtoTest extends EntityTest {

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
    public void validAccountShouldValidate() {
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankFirstNameShouldNotValidate() {
        accountJsonObject.replace("firstName", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void smallFirstNameShouldNotValidate() {
        accountJsonObject.replace("firstName", "a");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void longFirstNameShouldNotValidate() {
        accountJsonObject.replace("firstName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankLastNameShouldNotValidate() {
        accountJsonObject.replace("lastName", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void smallLastNameShouldNotValidate() {
        accountJsonObject.replace("lastName", "a");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void longLastNameShouldNotValidate() {
        accountJsonObject.replace("lastName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void nullBirthDateShouldNotValidate() {
        accountJsonObject.replace("birthDate", "2054-02-07");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankPhoneNumberShouldNotValidate() {
        accountJsonObject.replace("phoneNumber", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void smallPhoneNumberShouldNotValidate() {
        accountJsonObject.replace("phoneNumber", "4456");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void longPhoneNumberShouldNotValidate() {
        accountJsonObject.replace("phoneNumber", "888888888888888888888888888888888888888888888888888888888888888888" +
                "88888888888888888888888888888888888888888888888888888888888888888");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankEmailShouldNotValidate() {
        accountJsonObject.replace("email", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankPasswordShouldNotValidate() {
        accountJsonObject.replace("password", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void passwordUnder8CharShouldNotValidate() {
        accountJsonObject.replace("password", "1@Passw");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void passwordWithoutUpperCaseCharShouldNotValidate() {
        accountJsonObject.replace("password", "1@password");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void passwordWithoutLowerCharShouldNotValidate() {
        accountJsonObject.replace("password", "1@PASSWORD");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void passwordWithoutNumberShouldNotValidate() {
        accountJsonObject.replace("password", "@Password");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void passwordWithoutSpecialCharShouldNotValidate() {
        accountJsonObject.replace("password", "1Password");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankQuestionShouldNotValidate() {
        accountJsonObject.replace("securityQuestion", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }

    @Test
    public void blankAnswerShouldNotValidate() {
        accountJsonObject.replace("answerQuestion", "");
        try {
            accountDTO = mapper.readValue(accountJsonObject.toString(), AccountDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, accountDTO));
    }
}