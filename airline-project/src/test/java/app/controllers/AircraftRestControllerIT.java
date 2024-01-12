package app.controllers;

import app.dto.AircraftDto;
import app.mappers.AircraftMapper;
import app.repositories.AircraftRepository;
import app.services.AircraftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private AircraftMapper aircraftMapper;

    // Пагинация 2.0
    @Test
    void shouldGetAllAircraft() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/aircrafts"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllAircraftByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/aircrafts?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllAircraftByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/aircrafts?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPageAircraft() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/aircrafts?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(aircraftService
                        .getPage(pageable.getPageNumber(), pageable.getPageSize()).getContent())));
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/aircrafts?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/aircrafts?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    // Пагинация 2.0

    @Test
    void shouldSaveAircraft() throws Exception {
        var aircraft = new AircraftDto();
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
    void shouldGetAircraftById() throws Exception {
        long id = 2;
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(aircraftMapper.convertToAircarftDTOEntity(aircraftService.getAircraftById(id)))));
    }

    @Test
    void shouldEditById() throws Exception {
        long id = 2;
        var aircraft = aircraftMapper.convertToAircarftDTOEntity(aircraftService.getAircraftById(id));
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
