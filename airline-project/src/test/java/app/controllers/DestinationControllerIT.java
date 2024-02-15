package app.controllers;

import app.dto.AircraftDto;
import app.dto.DestinationDto;
import app.entities.Destination;
import app.enums.Airport;
import app.mappers.DestinationMapper;
import app.repositories.DestinationRepository;
import app.services.DestinationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.enums.Airport.CNN;
import static app.enums.Airport.RAT;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void shouldGetAllDestination() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/destinations"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllDestinationsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/destinations?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllDestinationsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPageDestinations() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destinationService
                        .getAllDestinations(pageable.getPageNumber(), pageable.getPageSize()))));
    }

    @Test
    void shouldCreateDestination() throws Exception {
        var destination = new DestinationDto(4L, Airport.VKO, "GMT +3");
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
        Page<DestinationDto> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0&size=10")
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
        Page destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertPageToJson(destination)));
    }

    @Test
    void shouldShowDestinationByPageable() throws Exception {
        var pageable = PageRequest.of(0, 3, Sort.by("id"));
        var city = "";
        var country = "Россия";
        var timezone = "";
        Page<DestinationDto> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
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
        Page<DestinationDto> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertPageToJson(destination)));
    }

    @Test
    void shouldShowDestinationByTimezoneNotFound() throws Exception {
        var pageable = PageRequest.of(0, 10, Sort.by("id"));
        var city = "";
        var country = "";
        var timezone = "gmt +3";
        Page<DestinationDto> destination = destinationService.getDestinationByNameAndTimezone(pageable.getPageNumber(), pageable.getPageSize(), city, country, timezone);
        mockMvc.perform(get("http://localhost:8080/api/destinations?page=0&size=10")
                        .param("cityName", city)
                        .param("countryName", country)
                        .param("timezone", timezone))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Transactional
    @Test
    void shouldUpdateDestination() throws Exception {
        Long id = 3L;
        long numberOfDestination = destinationRepository.count();
        DestinationDto rat = new DestinationDto();
        rat.setId(3L);
        rat.setAirportCode(RAT);
        rat.setTimezone("+3");
        mockMvc.perform(patch("http://localhost:8080/api/destinations/{id}", id)
                        .content(objectMapper.writeValueAsString(destinationMapper.toEntity(rat)))
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

    public String convertPageToJson(Page page) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Destination> destinations = page.getContent();
        int totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        boolean last = page.isLast();

        Map<String, Object> map = new HashMap<>();
        map.put("content", destinations);
        map.put("totalPages", totalPages);
        map.put("totalElements", totalElements);
        map.put("last", last);

        return mapper.writeValueAsString(map);
    }

    @Test
    @DisplayName("Проверка отсутствия возможности у пользователя изменить id места назначения при POST запросе")
    void shouldNotChangeIdByUserFromPostRequest() throws Exception {

        var destinationDto = new DestinationDto();
        destinationDto.setId(33L);
        destinationDto.setAirportCode(CNN);
        destinationDto.setTimezone("GMT +9");


        mockMvc.perform(post("http://localhost:8080/api/destinations")
                        .content(objectMapper.writeValueAsString(destinationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(not(33)));
    }

    @Test
    @DisplayName("Проверка отсутствия возможности у пользователя изменить id места назначения при PATCH запросе")
    void shouldNotChangeIdByUserFromPatchRequest() throws Exception {

        var destinationDto = new DestinationDto();
        destinationDto.setId(33L);
        destinationDto.setAirportCode(CNN);
        destinationDto.setTimezone("GMT +9");
        var id = 2L;

        mockMvc.perform(patch("http://localhost:8080/api/destinations/{id}", id)
                        .content(objectMapper.writeValueAsString(destinationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}
