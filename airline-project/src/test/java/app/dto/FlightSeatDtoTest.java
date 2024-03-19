package app.dto;

import app.entities.EntityTest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

import static app.enums.CategoryType.ECONOMY;

class FlightSeatDtoTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
    }

    private JSONObject initJSONObject() {
        var flightSeatJson = new JSONObject();
        SeatDto seat = new SeatDto();
        seat.setSeatNumber("1");
        seat.setId(1L);
        flightSeatJson.put("id", 1L);
        flightSeatJson.put("fare", 1500);
        flightSeatJson.put("isRegistered", true);
        flightSeatJson.put("isSold", true);
        flightSeatJson.put("isBooked", true);
        flightSeatJson.put("flightId", 1L);
        flightSeatJson.put("seat", seat);

        return flightSeatJson;
    }


    @Test
    void validFlightSeatShouldValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();

        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void negativeFareShouldNotValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("fare", -100);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullIsRegisteredShouldNotValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isRegistered", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
    @Test
    void nullIsSoldShouldNotValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isSold", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullFlightShouldNotValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("flightId", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullSeatShouldNotValidate() {
        FlightSeatDto testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("seat", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
}
