package app.controllers;

import app.dto.AircraftDto;
import app.mappers.AircraftMapper;
import app.repositories.AircraftRepository;
import app.services.AircraftService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

import static org.hamcrest.Matchers.not;

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

    // * * * * * * * * * * * Пагинация 2.0 * * * * * * * * * * * * * * * *
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
                        .getAllAircrafts(pageable.getPageNumber(), pageable.getPageSize()).getContent())));
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

    // * * * * * * * * * * * Save * * * * * * * * * * * * * * * *

    @Test
    @DisplayName("Should  save Aircraft")
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aircraftNumber").value("412584"))
                .andExpect(jsonPath("$.model").value("Boeing 777"))
                .andExpect(jsonPath("$.modelYear").value(2005))
                .andExpect(jsonPath("$.flightRange").value(2800));
    }

    @Test
    @DisplayName("Should ignoring id withing saving")
    void shouldSaveAircraftIgnoringId() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setId(5000L);
        aircraft.setAircraftNumber("412584");
        aircraft.setModel("Boeing 888");
        aircraft.setModelYear(2015);
        aircraft.setFlightRange(2800);

        mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(not(aircraft.getId())));
    }

    @Test
    @DisplayName("Should not save with invalid data")
    void shouldNotSaveAircraftWithInvalidInput() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setAircraftNumber("");
        aircraft.setModel("Boeing 777");
        aircraft.setModelYear(2005);

        mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // * * * * * * * * * * * Get * * * * * * * * * * * * * * * *

    @Test
    @DisplayName("Should get Aircraft by id from data base")
    void shouldGetAircraftById() throws Exception {
        long id = 2;
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(aircraftMapper.toDto(aircraftService.getAircraft(id)))));
    }

    @Test
    @DisplayName("Should not get Aircraft by not exist id")
    void shouldNotGetAircraftById() throws Exception {
        long id = 2000;
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get Aircraft by id after saving it")
    void shouldGetAircraftById_AfterSavingAircraft() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setAircraftNumber("412584");
        aircraft.setModel("Boeing 999");
        aircraft.setModelYear(2025);
        aircraft.setFlightRange(1000);

        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AircraftDto createdAircraft = objectMapper.readValue(responseContent, AircraftDto.class);
        Long id = createdAircraft.getId();

        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aircraftNumber").value("412584"))
                .andExpect(jsonPath("$.model").value("Boeing 999"))
                .andExpect(jsonPath("$.modelYear").value(2025))
                .andExpect(jsonPath("$.flightRange").value(1000));
    }

    // * * * * * * * * * * * Update * * * * * * * * * * * * * * * *
    @Test
    @DisplayName("Should update Aircraft by id from data base")
    void shouldUpdateById() throws Exception {
        long id = 2;
        var aircraft = aircraftMapper.toDto(aircraftService.getAircraft(id));
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
    @DisplayName("Should not update Aircraft with invalid data")
    void shouldNotUpdateById() throws Exception {
        long id = 2;
        var updatedAircraftDTO = new AircraftDto();
        updatedAircraftDTO.setAircraftNumber("531487");

        mockMvc.perform(patch("http://localhost:8080/api/aircrafts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedAircraftDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update Aircraft by id after saving it")
    void shouldUpdateAircraftById_AfterSavingAircraft() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("ABC123");
        aircraft.setModel("Boeing 777");
        aircraft.setModelYear(2020);
        aircraft.setFlightRange(5000);
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AircraftDto createdAircraft = objectMapper.readValue(responseContent, AircraftDto.class);
        Long id = createdAircraft.getId();

        var updatedAircraftDTO = new AircraftDto();
        updatedAircraftDTO.setAircraftNumber("ABC123");
        updatedAircraftDTO.setModel("Airbus A320");
        updatedAircraftDTO.setModelYear(2018);
        updatedAircraftDTO.setFlightRange(4000);

        mockMvc.perform(patch("http://localhost:8080/api/aircrafts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedAircraftDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aircraftNumber").value("ABC123"))
                .andExpect(jsonPath("$.model").value("Airbus A320"))
                .andExpect(jsonPath("$.modelYear").value(2018))
                .andExpect(jsonPath("$.flightRange").value(4000));
    }

    @Test
    @DisplayName("Should update Aircraft by id after saving it ignoring transfer id")
    void shouldUpdateAircraftById_IgnoringId() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setId(1L);
        aircraft.setAircraftNumber("ABC123");
        aircraft.setModel("Boeing 777");
        aircraft.setModelYear(2020);
        aircraft.setFlightRange(5000);
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AircraftDto createdAircraft = objectMapper.readValue(responseContent, AircraftDto.class);
        Long id = createdAircraft.getId();

        var updatedAircraftDTO = new AircraftDto();
        updatedAircraftDTO.setId(5L);
        updatedAircraftDTO.setAircraftNumber("XYZ789");
        updatedAircraftDTO.setModel("Airbus A320");
        updatedAircraftDTO.setModelYear(2018);
        updatedAircraftDTO.setFlightRange(4000);

        mockMvc.perform(patch("http://localhost:8080/api/aircrafts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedAircraftDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(not(aircraft.getId())))
                .andExpect(jsonPath("$.id").value(not(updatedAircraftDTO.getId())))
                .andExpect(jsonPath("$.aircraftNumber").value("XYZ789"))
                .andExpect(jsonPath("$.model").value("Airbus A320"))
                .andExpect(jsonPath("$.modelYear").value(2018))
                .andExpect(jsonPath("$.flightRange").value(4000));
    }

    @Test
    @DisplayName("Should not update Aircraft with not exist id")
    void shouldNotUpdateAircraftById() throws Exception {
        var updatedAircraftDTO = new AircraftDto();
        updatedAircraftDTO.setAircraftNumber("XYZ789");
        updatedAircraftDTO.setModel("Airbus A320");
        updatedAircraftDTO.setModelYear(2018);
        updatedAircraftDTO.setFlightRange(4000);

        long id = 2000;

        mockMvc.perform(patch("http://localhost:8080/api/aircrafts/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedAircraftDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // * * * * * * * * * * * Delete * * * * * * * * * * * * * * * *

    @Test
    @DisplayName("Should delete exist Aircraft by id from data base")
    void shouldDeleteById() throws Exception {
        long id = 2;
        mockMvc.perform(delete("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete Aircraft by not exist id")
    void shouldNotDeleteAircraftById() throws Exception {
        long id = 2000;
        mockMvc.perform(delete("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete Aircraft by id after saving it")
    void shouldDeleteAircraftById() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setAircraftNumber("ABC123");
        aircraft.setModel("Boeing 777");
        aircraft.setModelYear(2020);
        aircraft.setFlightRange(5000);
        MvcResult result = mockMvc.perform(post("http://localhost:8080/api/aircrafts")
                        .content(objectMapper.writeValueAsString(aircraft))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AircraftDto createdAircraft = objectMapper.readValue(responseContent, AircraftDto.class);
        Long id = createdAircraft.getId();

        mockMvc.perform(delete("http://localhost:8080/api/aircrafts/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}