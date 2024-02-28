package app.dto;

import app.entities.EntityTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

class AircraftDtoTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void blankAircraftNumberFieldShouldNotValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("");
        aircraft.setModel("boeing354A");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);


        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void notBlankAircraftNumberFieldShouldValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing354A");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void blankModelFieldShouldNotValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void notBlankModelFieldShouldValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing345");
        aircraft.setModelYear(2054);
        aircraft.setFlightRange(250);
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void lessThanMinModelYearFieldShouldNotValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2);
        aircraft.setFlightRange(250);
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void moreThanMinModelYearFieldShouldNotValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);
        aircraft.setFlightRange(250);
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void nullFlightRangeFieldShouldNotValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);

        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, aircraft));
    }

    @Test
    void notNullFlightRangeFieldShouldValidate() {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("435HA");
        aircraft.setModel("boeing435");
        aircraft.setModelYear(2005);
        aircraft.setFlightRange(200);

        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, aircraft));
    }
}