package app.controllers;

import app.dto.AircraftDTO;
import app.repositories.AircraftRepository;
import app.services.interfaces.AircraftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-aircraftCategorySeat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class AircraftRestControllerIT extends IntegrationTestBase {

    @Autowired
    private AircraftService aircraftService;
    @Autowired
    private AircraftRepository aircraftRepository;

    @Test
    void shouldSaveAircraft() throws Exception {
        var aircraft = new AircraftDTO();
        aircraft.setAircraftNumber("412584");
        aircraft.setModel("Boeing 777");
        aircraft.setModelYear(2005);
        aircraft.setFlightRange(2800);

        mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void shouldGetAllAircraft() throws Exception {
        mockMvc.perform(
                        get("http://localhost:8080/api/aircrafts"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAircraftById() throws Exception {
        long id = 2;
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(new AircraftDTO(aircraftService.getAircraftById(id)))));
    }

    @Test
    void shouldEditById() throws Exception {
        long id = 2;
        var aircraft = new AircraftDTO(aircraftService.getAircraftById(id));
        aircraft.setAircraftNumber("531487");
        aircraft.setModel("Boeing 737");
        aircraft.setModelYear(2001);
        aircraft.setFlightRange(5000);
        long numberOfAircraft = aircraftRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/aircrafts/{id}", id)
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(aircraft)))
                .andExpect(result -> assertThat(aircraftRepository.count(), equalTo(numberOfAircraft)));
    }

    @Test
    void shouldDeleteById() throws Exception {
        long id = 2;
        mockMvc.perform(delete("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
