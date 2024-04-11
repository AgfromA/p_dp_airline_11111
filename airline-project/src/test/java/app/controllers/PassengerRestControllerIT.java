package app.controllers;

import app.dto.PassengerDto;
import app.entities.Passenger;
import app.entities.Passport;
import app.enums.Gender;
import app.mappers.PassengerMapper;
import app.repositories.PassengerRepository;
import app.services.PassengerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;


import java.time.LocalDate;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-passenger-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PassengerRestControllerIT extends IntegrationTestBase {

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private PassengerRepository passengerRepository;

    private final Random random = new Random();

    public Passenger createPassenger() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("Andrey");
        passenger.setLastName("Ivanov");
        passenger.setPhoneNumber("89033333333");
        passenger.setEmail(random.nextInt() + "@mail.ru");
        passenger.setBirthDate(LocalDate.parse("2000-12-11"));

        Passport passport = new Passport();
        passport.setGender(Gender.MALE);
        passport.setSerialNumberPassport("1800 303400");
        passport.setPassportIssuingCountry("Russia");
        passport.setPassportIssuingDate(LocalDate.parse("2014-12-11"));
        passport.setMiddleName("Ivanovich");
        passenger.setPassport(passport);
        passengerRepository.save(passenger);

        return passenger;
    }

    // Пагинация 2.0
    @Test
    void shouldGetAllPassengers() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/passengers"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPassengersByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/passengers?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllPassengersByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPagePassengers() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengers(pageable))));
    }
    // Пагинация 2.0

    @Test
    @DisplayName("Get passenger by ID")
    void shouldGetPassengerById() throws Exception {
        var passenger = createPassenger();
        var id = passenger.getId();
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(passengerMapper.toDto(
                                passengerService.getPassenger(id).get()))));
    }

    @Test
    @DisplayName("Post new passenger")
    void shouldAddNewPassenger() throws Exception {

        Passenger passenger = new Passenger();
        passenger.setFirstName("Igor");
        passenger.setLastName("Igorev");
        passenger.setPhoneNumber("89033333355");
        passenger.setEmail("igorev@mail.ru");
        passenger.setBirthDate(LocalDate.parse("2001-12-11"));

        Passport passport = new Passport();
        passport.setGender(Gender.MALE);
        passport.setSerialNumberPassport("1810 322300");
        passport.setPassportIssuingCountry("Russia");
        passport.setPassportIssuingDate(LocalDate.parse("2014-12-01"));
        passport.setMiddleName("Igor");
        passenger.setPassport(passport);

        passenger.setId(0L);
        var passengerDto = passengerMapper.toDto(passenger);

        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Post exist passenger")
    void shouldAddExistPassenger() throws Exception {
        var passenger = createPassenger();
        var id = passenger.getId();
        var passengerDto = new PassengerDto();
        passengerDto.setId(id);
        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete passenger by ID and check passenger with deleted ID")
    void shouldDeletePassenger() throws Exception {
        Passenger passenger = createPassenger();
        var id = passenger.getId();
        mockMvc.perform(delete("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update passenger")
    void shouldUpdatePassenger() throws Exception {
        PassengerDto passengerDto = passengerMapper.toDto(createPassenger());
        var id = passengerDto.getId();
        passengerDto.setFirstName("Klark");
        long numberOfPassenger = passengerRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/passengers/{id}", id)
                        .content(objectMapper.writeValueAsString(passengerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(passengerRepository.count(), equalTo(numberOfPassenger)));
    }


    @Test
    @DisplayName("Filter passenger by FirstName and LastName")
    void shouldShowPassengerByFirstNameAndLastName() throws Exception {
        createPassenger();
        var pageable = PageRequest.of(0, 10);
        var firstName = "Andrey";
        var lastName = "Ivanov";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengersFiltered(pageable, firstName, lastName, email, passportSerialNumber))));
    }

    @Test
    @DisplayName("Filter passenger by FirstName")
    void shouldShowPassengerByFirstName() throws Exception {
        var pageable = PageRequest.of(0, 10);
        var firstName = "John20";
        var lastName = "";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengersFiltered(pageable, firstName, lastName, email, passportSerialNumber))));
    }

    @Test
    @DisplayName("Filter passenger by LastName")
    void shouldShowPassengerByLastName() throws Exception {
        var pageable = PageRequest.of(0, 10);
        var firstName = "";
        var lastName = "Simons20";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengersFiltered(pageable, firstName, lastName, email, passportSerialNumber))));
    }

    @Test
    @DisplayName("Filter passenger by Email")
    void shouldShowPassengerByEmail() throws Exception {
        var pageable = PageRequest.of(0, 10);
        var firstName = "";
        var lastName = "";
        var email = "passenger20@mail.ru";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengersFiltered(pageable, firstName, lastName, email, passportSerialNumber))));
    }

    @Test
    @DisplayName("Filter passenger by serialNumberPassport")
    void shouldShowPassengerByPassportSerialNumber() throws Exception {
        var pageable = PageRequest.of(0, 10);
        var firstName = "";
        var lastName = "";
        var email = "";
        var serialNumberPassport = "0011 001800";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("serialNumberPassport", serialNumberPassport))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPassengersFiltered(pageable, firstName, lastName, email, serialNumberPassport))));
    }

    @Test
    @DisplayName("Filter passenger by FirstName not found in database")
    void shouldShowPassengerByFirstNameNotFoundInDatabase() throws Exception {
        var firstName = "aaa";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Filter passenger by lastName not found in database")
    void shouldShowPassengerByLastNameNotFoundInDatabase() throws Exception {
        var lastName = "aaa";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Filter passenger by email not found in database")
    void shouldShowPassengerByEmailNotFoundInDatabase() throws Exception {
        var email = "aaa@aaa.com";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Filter passenger by serialNumberPassport not found in database")
    void shouldShowPassengerByPassportSerialNumberNotFoundInDatabase() throws Exception {
        var serialNumberPassport = "7777 777777";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("serialNumberPassport", serialNumberPassport))
                .andDo(print())
                .andExpect(status().isOk());
    }


    // Пытаемся добавить пассажира с таким же email
    @Test
    void shouldAddPassengerWithDublicateEmail() throws Exception {
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
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}