package app.exceptions;

import app.controllers.IntegrationTestBase;
import app.dto.ExceptionResponseDto;
import app.entities.Passenger;
import app.entities.Passport;
import app.enums.Gender;
import app.mappers.PassengerMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-passenger-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GeneralExceptionHandlerTestIT extends IntegrationTestBase {

    @Autowired
    private PassengerMapper passengerMapper;

    // Пытаемся добавить пассажира с таким же email
    @Test
    void testHandleConstraintViolationException() throws Exception {
        var passenger2 = new Passenger();
        passenger2.setFirstName("Bob");
        passenger2.setLastName("Bobbov");
        passenger2.setPhoneNumber("89033333553");
        passenger2.setEmail("cartman@mail.ru");
        passenger2.setBirthDate(LocalDate.parse("2010-12-11"));

        Passport passport = new Passport();
        passport.setGender(Gender.MALE);
        passport.setSerialNumberPassport("1810 303400");
        passport.setPassportIssuingCountry("Russia");
        passport.setPassportIssuingDate(LocalDate.parse("2004-12-11"));
        passport.setMiddleName("Bobo");
        passenger2.setPassport(passport);

        var passengerDto = passengerMapper.toDto(passenger2);

        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email already exists"));
    }

    @Test
    void testHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(result -> {
                    ExceptionResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ExceptionResponseDto.class);
                    Assertions.assertEquals("Page index must not be less than zero", responseDto.getMessage());
                    Assertions.assertNotNull(responseDto.getRequestId());
                });
    }

    @Test
    void testHandleValidationException() throws Exception {
        var passenger2 = new Passenger();
        passenger2.setFirstName("B");
        passenger2.setLastName("Bobbov");
        passenger2.setPhoneNumber("89033333553");
        passenger2.setEmail("newpass@mail.ru");
        passenger2.setBirthDate(LocalDate.parse("2010-12-11"));

        Passport passport = new Passport();
        passport.setGender(Gender.MALE);
        passport.setSerialNumberPassport("1810 303411");
        passport.setPassportIssuingCountry("Russia");
        passport.setPassportIssuingDate(LocalDate.parse("2004-01-11"));
        passport.setMiddleName("BBBB");
        passenger2.setPassport(passport);

        var passengerDto = passengerMapper.toDto(passenger2);
        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
        // TODO добавить проверку на содержимое тела ответа
    }
}