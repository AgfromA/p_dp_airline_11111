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

class FlightSeatDTOTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
    }

    private JSONObject initJSONObject() {
        var flightSeatJson = new JSONObject();
        SeatDTO seat = new SeatDTO();
        seat.setSeatNumber("1");
        seat.setId(1L);
        flightSeatJson.put("id", 1L);
        flightSeatJson.put("fare", 1500);
        flightSeatJson.put("isRegistered", true);
        flightSeatJson.put("isSold", true);
        flightSeatJson.put("isBooked", true);
        flightSeatJson.put("flightId", 1L);
        flightSeatJson.put("seat", seat);
        flightSeatJson.put("category", ECONOMY);

        return flightSeatJson;
    }


    @Test
    void validFlightSeatShouldValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();

        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void negativeFareShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("fare", -100);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullIsRegisteredShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isRegistered", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
    @Test
    void nullIsSoldShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("isSold", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullFlightShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("flightId", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }

    @Test
    void nullSeatShouldNotValidate() {
        FlightSeatDTO testFlightSeat;
        JSONObject flightSeatJson = initJSONObject();
        flightSeatJson.replace("seat", null);
        try {
            testFlightSeat = mapper.readValue(flightSeatJson.toString(), FlightSeatDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testFlightSeat));
    }
}
