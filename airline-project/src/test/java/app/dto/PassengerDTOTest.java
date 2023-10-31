package app.dto;

import app.entities.EntityTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class PassengerDTOTest extends EntityTest {

    private Validator validator;
    private PassengerDTO passenger;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        try (var validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        passenger = new PassengerDTO();
    }

    private JSONObject initJSONObject() {
        var jsonObject = new JSONObject();
        JSONObject jsonObjectPassport = new JSONObject();
        jsonObjectPassport.put("middleName", "Bagin");
        jsonObjectPassport.put("gender", "male");
        jsonObjectPassport.put("serialNumberPassport", "9241 444977");
        jsonObjectPassport.put("passportIssuingDate", "2012-02-01");
        jsonObjectPassport.put("passportIssuingCountry", "Russia");
        jsonObject.put("id", 1004L);
        jsonObject.put("firstName", "Alexandr");
        jsonObject.put("lastName", "Bagin");
        jsonObject.put("birthDate", "1998-01-08");
        jsonObject.put("phoneNumber", "89995252503");
        jsonObject.put("email", "passenger@mail.ru");
        jsonObject.put("passport", jsonObjectPassport);
        return jsonObject;
    }

    @Test
    public void testRightPassenger() {
        try {
            passenger = mapper.readValue(initJSONObject().toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankFirstNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("firstName", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallFirstNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("firstName", "a");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankLastNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("lastName", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallLastNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("lastName", "a");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void nullBirthDateShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("birthDate", null);
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void pastBirthDateShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("birthDate", "2054-02-07");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankPhoneNumberShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void smallPhoneNumberShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "4456");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longFirstNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("firstName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longLastNameShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("lastName", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void longPhoneNumberShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("phoneNumber", "888888888888888888888888888888888888888888888888888888888888888888" +
                "88888888888888888888888888888888888888888888888888888888888888888");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void blankEmailShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("email", "");
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }

    @Test
    public void nullPassportShouldNotValidate() {
        var jsonObject = initJSONObject();
        jsonObject.replace("passport", null);
        try {
            passenger = mapper.readValue(jsonObject.toString(), PassengerDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, passenger));
    }
}