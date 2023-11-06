package app.dto;

import app.entities.EntityTest;
import app.enums.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class DestinationDTOTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void airportNullShouldValidate() {
        var testDestination = new DestinationDTO(1L, null, "GMT +99999");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }
    @Test
    public void between2And9CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDTO(1L, Airport.AER, "GMT + 3");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testDestination));
    }

    @Test
    public void leesThan2CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDTO(1L, Airport.AER, "3");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }

    @Test
    public void moreThan9CharTimezoneSizeShouldValidate() {
        var testDestination = new DestinationDTO(1L, Airport.AER, "GMT +99999");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testDestination));
    }


}
