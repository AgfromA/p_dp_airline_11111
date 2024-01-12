package app.dto;

import app.entities.EntityTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

public class TimezoneDtoTest extends EntityTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Test
    public void lessThan3CharCountryNameSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Р", "Москва", "GMT+3", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void between3And30CharCountryNameSizeShouldValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "GMT+4");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void moreThan30CharCountryNameSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "РоссияМатушкаМояПравославнаяПрекрасная", "Москва", "GMT+3", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void lessThan3CharCityNameSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "М", "GMT+3", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void between3And30CharCityNameSizeShouldValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "GMT+4");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void moreThan30CharCityNameSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "МоскваПрекраснаяВеликаяСтолицаРоссии", "GMT+3", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void lessThan2CharGmtSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "3", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void between2And9CharGmtSizeShouldValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "GMT+4");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void moreThan9CharGmtSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT   +12:30", "GMT+4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void lessThan2CharGmtWinterSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "4");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void between2And9CharGmtWinterSizeShouldValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "GMT+4");
        Assertions.assertTrue(isSetWithViolationIsEmpty(validator, testTimezone));
    }

    @Test
    public void moreThan9CharGmtWinterSizeShouldNotValidate() {
        var testTimezone = new TimezoneDto(1L, "Россия", "Москва", "GMT+3", "GMT      +11");
        Assertions.assertFalse(isSetWithViolationIsEmpty(validator, testTimezone));
    }
}