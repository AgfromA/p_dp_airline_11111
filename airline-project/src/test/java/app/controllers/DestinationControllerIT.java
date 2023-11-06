package app.controllers;

import app.dto.DestinationDTO;
import app.enums.Airport;
import app.mappers.DestinationMapper;
import app.repositories.DestinationRepository;
import app.services.interfaces.DestinationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static app.enums.Airport.RAT;
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
@Sql(value = {"/sqlQuery/create-destination-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DestinationControllerIT extends IntegrationTestBase {


    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private DestinationMapper destinationMapper;

    @Test
    void shouldCreateDestination() throws Exception {
        var destination = new DestinationDTO(4L, Airport.VKO, "GMT +3");
        System.out.println(objectMapper.writeValueAsString(destination));
        mockMvc.perform(post("http://localhost:8080/api/destinations")
                        .content(objectMapper.writeValueAsString(destination))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldShowDestinationByName() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var city = "Абакан";
        var country = "";
        var timezone = "";
        Page<DestinationDTO> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destination)));
    }

    @Test
    void shouldShowDestinationByCountry() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var city = "";
        var country = "Россия";
        var timezone = "";
        Page<DestinationDTO> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destination)));
    }

    @Test
    void shouldShowDestinationByPageable() throws Exception {
        var pageable = PageRequest.of(0, 3, Sort.by("id"));
        var city = "";
        var country = "Россия";
        var timezone = "";
        Page<DestinationDTO> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0&size=3")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destination)));
    }

    @Test
    void shouldShowDestinationByTimezone() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var city = "";
        var country = "";
        var timezone = "+3";
        Page<DestinationDTO> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destination)));
    }

    @Test
    void shouldShowDestinationByTimezoneNotFound() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var city = "";
        var country = "";
        var timezone = "gmt +3";
        Page<DestinationDTO> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void shouldUpdateDestination() throws Exception {
        Long id = 3L;
        long numberOfDestination = destinationRepository.count();
        DestinationDTO rat = new DestinationDTO();
        rat.setId(3L);
        rat.setAirportCode(RAT);
        rat.setTimezone("+3");
        mockMvc.perform(patch("http://localhost:8080/api/destinations/{id}", id)
                        .content(objectMapper.writeValueAsString(destinationMapper.convertToDestinationEntity(rat)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(result -> assertThat(destinationRepository.count(), equalTo(numberOfDestination)));
    }

    @Test
    void shouldDeleteDestinationById() throws Exception {
        Long id = 3L;
        mockMvc.perform(delete("http://localhost:8080/api/destinations/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
