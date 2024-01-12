package app.dto;

import app.entities.EntityTest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


public class SeatDtoTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void wrongSeatNumberTest() {
        SeatDto seat;
        var mapper = new ObjectMapper();
        var jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4");
        jsonObject.put("isNearEmergencyExit", false);
        jsonObject.put("isLockedBack", true);
        jsonObject.put("category", "FIRST");
        jsonObject.put("aircraftId", 1);
        String testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));
    }

    @Test
    public void wrongSeatIsNearEmergencyExitTest() {
        SeatDto seat;
        var mapper = new ObjectMapper();
        var jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4A");
        jsonObject.put("isNearEmergencyExit", null);
        jsonObject.put("isLockedBack", true);
        jsonObject.put("category", "FIRST");
        jsonObject.put("aircraftId", 1);
        String testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));
    }

    @Test
    public void wrongSeatIsLockedBackTest() {
        SeatDto seat;
        var mapper = new ObjectMapper();
        var jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("seatNumber", "4A");
        jsonObject.put("isNearEmergencyExit", false);
        jsonObject.put("isLockedBack", null);
        jsonObject.put("category", "FIRST");
        jsonObject.put("aircraftId", 1);
        var testJSON = jsonObject.toString();

        try {
            seat = mapper.readValue(testJSON, SeatDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, seat));

    }
}
