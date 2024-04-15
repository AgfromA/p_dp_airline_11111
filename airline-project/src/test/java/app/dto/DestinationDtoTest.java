package app.dto;

import app.entities.EntityTest;
import app.enums.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class DestinationDtoTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void airportNullShouldValidate() {
        var testDestination = new DestinationDto(4L, null, "GMT +3", "Россия", "Москва", "Внуково");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }
    @Test
    public void between2And9CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDto(4L, Airport.VKO, "GMT + 3", "Россия", "Москва", "Внуково");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testDestination));
    }

    @Test
    public void leesThan2CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDto(4L, Airport.VKO, "3", "Россия", "Москва", "Внуково");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }

    @Test
    public void moreThan9CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDto(4L, Airport.VKO, "GMT +99999", "Россия", "Москва", "Внуково");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }


}
