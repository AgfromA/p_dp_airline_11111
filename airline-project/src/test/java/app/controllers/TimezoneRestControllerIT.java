package app.controllers;

import app.dto.TimezoneDto;
import app.mappers.TimezoneMapper;
import app.repositories.TimezoneRepository;
import app.services.interfaces.TimezoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
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
@Sql(value = {"/sqlQuery/create-timezone-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class TimezoneRestControllerIT extends IntegrationTestBase {

    @Autowired
    private TimezoneService timezoneService;
    @Autowired
    private TimezoneRepository timezoneRepository;

    private final TimezoneMapper timezoneMapper = Mappers.getMapper(TimezoneMapper.class);

    // Пагинация 2.0
    @Test
    void shouldGetAllTimezones() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/timezones"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllTimezonesByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/timezones?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllTimezonesByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/timezones?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/timezones?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/timezones?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPageTickets() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/timezones?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(timezoneService
                        .getAllPagesTimezones(pageable.getPageNumber(), pageable.getPageSize())
                        .getContent())));
    }
    // Пагинация 2.0

    @Test
    @DisplayName("Creating Timezone")
    void shouldCreateNewTimezone() throws Exception {
        var timezoneDto = new TimezoneDto();
        timezoneDto.setCityName("New-York");
        timezoneDto.setCountryName("USA");
        timezoneDto.setGmt("GMT-5");
        timezoneDto.setGmtWinter("GMT-4");
        mockMvc.perform(post("http://localhost:8080/api/timezones")
                        .content(objectMapper.writeValueAsString(timezoneDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Get Timezone by ID")
    void shouldGetTimezoneById() throws Exception {
        long id = 3L;
        mockMvc.perform(get("http://localhost:8080/api/timezones/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new TimezoneDto()))
                );
    }

    @Test
    @DisplayName("Delete Timezone by ID")
    void shouldDeleteById() throws Exception {
        Long id = 3L;
        mockMvc.perform(delete("http://localhost:8080/api/timezones/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
        mockMvc.perform(get("http://localhost:8080/api/timezones/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update timezone")
    void shouldUpdateTimezone() throws Exception {
        long id = 5L;
        var timezoneDto = timezoneService.getTimezoneById(id).get();
        timezoneDto.setCountryName("Чехия");
        long numberOfTimezone = timezoneRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/timezones/{id}", id)
                        .content(objectMapper.writeValueAsString(timezoneDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(timezoneRepository.count(), equalTo(numberOfTimezone)));
    }
}