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


class BookingDTOTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    private JSONObject initJSONObject() {
        var bookingDtoJSON = new JSONObject();
        bookingDtoJSON.put("id", 10000L);
        bookingDtoJSON.put("bookingDate", "2023-01-20T17:02:05.003992");
        bookingDtoJSON.put("passengerId", 1000L);
        bookingDtoJSON.put("flightId", 1L);
        bookingDtoJSON.put("categoryType", "BUSINESS");

        return bookingDtoJSON;
    }


    @Test
    public void validBookingShouldValidate() {
        BookingDTO testBooking;
        var bookingJson = initJSONObject();

        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullIdPassengerShouldNotValidate() {
        BookingDTO testBooking;
        var bookingJson = initJSONObject();
        bookingJson.replace("passengerId", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullFlightShouldNotValidate() {
        BookingDTO testBooking;
        var bookingJson = initJSONObject();
        bookingJson.replace("flightId", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }

    @Test
    public void nullCategoryShouldNotValidate() {
        BookingDTO testBooking;
        var bookingJson = initJSONObject();
        bookingJson.replace("categoryType", null);
        try {
            testBooking = mapper.readValue(bookingJson.toString(), BookingDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testBooking));
    }
}