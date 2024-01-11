package app.controllers;

import app.dto.PassengerDTO;
import app.entities.Passport;
import app.enums.Gender;
import app.repositories.PassengerRepository;
import app.services.interfaces.PassengerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-passenger-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PassengerRestControllerIT extends IntegrationTestBase {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private PassengerRepository passengerRepository;

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
                        .getAllPagesPassengers(pageable)
                        .getContent())));
    }
    // Пагинация 2.0

    @Test
    @DisplayName("Get passenger by ID")
    void shouldGetPassengerById() throws Exception {
        var id = 4L;
        mockMvc.perform(
                        get("http://localhost:8080/api/passengers/{id}", id))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(new PassengerDTO(passengerService.getPassengerById(id).get())))
                );

    }

    @Test
    @DisplayName("Post new passenger")
    void shouldAddNewPassenger() throws Exception {
        var passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName("Petr");
        passengerDTO.setLastName("Petrov");
        passengerDTO.setBirthDate(LocalDate.of(2023, 3, 23));
        passengerDTO.setPhoneNumber("79222222222");
        passengerDTO.setEmail("petrov@mail.ru");
        passengerDTO.setPassport(new Passport("Petr", Gender.MALE, "3333 123456", LocalDate.of(2006, 3, 30), "Russia"));

        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Post exist passenger")
    void shouldAddExistPassenger() throws Exception {
        var passengerDTO = new PassengerDTO();
        passengerDTO.setId(4L);
        mockMvc.perform(
                        post("http://localhost:8080/api/passengers")
                                .content(objectMapper.writeValueAsString(passengerDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete passenger by ID and check passenger with deleted ID")
    void shouldDeletePassenger() throws Exception {
        var id = 4L;
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
        var id = 4L;
        var passengerDTO = new PassengerDTO(passengerService.getPassengerById(4L).get());
        passengerDTO.setFirstName("Klark");
        long numberOfPassenger = passengerRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/passengers/{id}", id)
                        .content(objectMapper.writeValueAsString(passengerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(passengerRepository.count(), equalTo(numberOfPassenger)));
    }


    @Test
    @DisplayName("Filter passenger by FirstName and LastName")
    void shouldShowPassengerByFirstNameAndLastName() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var firstName = "John20";
        var lastName = "Simons20";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, passportSerialNumber).getContent())));
    }

    @Test
    @DisplayName("Filter passenger by FirstName")
    void shouldShowPassengerByFirstName() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var firstName = "John20";
        var lastName = "";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, passportSerialNumber).getContent())));
    }

    @Test
    @DisplayName("Filter passenger by LastName")
    void shouldShowPassengerByLastName() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var firstName = "";
        var lastName = "Simons20";
        var email = "";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, passportSerialNumber).getContent())));
    }

    @Test
    @DisplayName("Filter passenger by Email")
    void shouldShowPassengerByEmail() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var firstName = "";
        var lastName = "";
        var email = "passenger20@mail.ru";
        var passportSerialNumber = "";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, passportSerialNumber).getContent())));
    }

    @Test
    @DisplayName("Filter passenger by serialNumberPassport")
    void shouldShowPassengerByPassportSerialNumber() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var firstName = "";
        var lastName = "";
        var email = "";
        var serialNumberPassport = "0011 001800";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("serialNumberPassport", serialNumberPassport))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(passengerService
                        .getAllPagesPassengerByKeyword(pageable, firstName, lastName, email, serialNumberPassport).getContent())));
    }

    @Test
    @DisplayName("Filter passenger by FirstName not found in database")
    void shouldShowPassengerByFirstNameNotFoundInDatabase() throws Exception {
        var firstName = "aaa";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("firstName", firstName))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Filter passenger by lastName not found in database")
    void shouldShowPassengerByLastNameNotFoundInDatabase() throws Exception {
        var lastName = "aaa";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("lastName", lastName))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Filter passenger by email not found in database")
    void shouldShowPassengerByEmailNotFoundInDatabase() throws Exception {
        var email = "aaa@aaa.com";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Filter passenger by serialNumberPassport not found in database")
    void shouldShowPassengerByPassportSerialNumberNotFoundInDatabase() throws Exception {
        var serialNumberPassport = "7777 777777";
        mockMvc.perform(get("http://localhost:8080/api/passengers?page=0&size=10")
                        .param("serialNumberPassport", serialNumberPassport))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
