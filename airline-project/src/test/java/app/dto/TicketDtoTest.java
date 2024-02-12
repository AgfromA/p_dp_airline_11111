package app.dto;

import app.entities.EntityTest;
import app.enums.Airport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class TicketDtoTest extends EntityTest {

    private Validator validator;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private JSONObject initJsonObject() {

        var ticketJson = new JSONObject();
        ticketJson.put("id", 1);
        ticketJson.put("ticketNumber", "SD-2222");
        ticketJson.put("passengerId", 4);
        ticketJson.put("firstName", "TestName");
        ticketJson.put("lastName", "TestLastName");
        ticketJson.put("code", "XXX");
        ticketJson.put("from", Airport.AER);
        ticketJson.put("to", Airport.AAQ);
        ticketJson.put("departureDateTime", "2023-01-20T17:02:05.003992");
        ticketJson.put("arrivalDateTime", "2023-01-20T17:02:05.003992");
        ticketJson.put("flightSeatId", 1);
        ticketJson.put("seatNumber", "1A");
        ticketJson.put("bookingId", 3);
        return ticketJson;
    }

    @Test
    public void validTicketShouldValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerIdFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("passengerId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerFirstNameFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("firstName", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortPassengerFirstNameFieldShouldNotValidate() {

        TicketDto ticketDTO;
        JSONObject ticketJson = initJsonObject();
        ticketJson.replace("firstName", "I");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullPassengerLastNameFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("lastName", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortPassengerLastNameFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("lastName", "I");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullCodeFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("code", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortCodeFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("code", "1");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullFromFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("from", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullToFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("to", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullDepartureDateTimeFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("departureDateTime", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullArrivalDateTimeFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("arrivalDateTime", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullFlightSeatIdFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("flightSeatId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullBookingIdFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("bookingId", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void nullSeatNumberFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("seatNumber", null);
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }

    @Test
    public void shortSeatNumberFieldShouldNotValidate() {

        TicketDto ticketDTO;
        var ticketJson = initJsonObject();
        ticketJson.replace("seatNumber", "1");
        try {
            ticketDTO = mapper.readValue(ticketJson.toString(), TicketDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e + "Exception during mapping from JSON");
        }
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, ticketDTO));
    }
}
