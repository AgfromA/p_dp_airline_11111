package app.controllers;


import app.services.interfaces.FlightService;
import app.util.mappers.FlightMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flight-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightRestControllerIT extends IntegrationTestBase {

    @Autowired
    private FlightService flightService;
    @Autowired
    private FlightMapper flightMapper;


    @Test
    void showAllFlights_test() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sqlQuery/delete-from-tables.sql"})
    void showAllFlights_testError() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    void showFlightsByCityFrom() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String cityFrom = "Волгоград";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("cityFrom", cityFrom))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(cityFrom, null, null, null, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByCityTo() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String cityTo = "Омск";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("cityTo", cityTo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(null, cityTo, null, null, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByDateStart() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String dateStart = "2022-11-23T04:30:00";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("dateStart", dateStart))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(null, null, dateStart, null, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByDateFinish() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String dateFinish = "2022-11-23T07:30:00";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("dateFinish", dateFinish))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(null, null, null, dateFinish, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByCityFromAndCityTo() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String cityFrom = "Волгоград";
        String cityTo = "Омск";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("cityFrom", cityFrom)
                        .param("cityTo", cityTo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, null, null, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByDateStartAndDateFinish() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String dateStart = "2022-11-23T04:30:00";
        String dateFinish = "2022-11-23T07:30:00";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("dateStart", dateStart)
                        .param("dateFinish", dateFinish))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(null, null, dateStart, dateFinish, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByCityFromAndDateStart() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String cityFrom = "Волгоград";
        String dateStart = "2022-11-23T04:30:00";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("cityFrom", cityFrom)
                        .param("dateStart", dateStart))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(cityFrom, null, dateStart, null, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

    @Test
    void showFlightsByAllParams() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        String cityFrom = "Волгоград";
        String cityTo = "Омск";
        String dateStart = "2022-11-23T04:30:00";
        String dateFinish = "2022-11-23T07:30:00";
        mockMvc.perform(get("http://localhost:8080/api/flights/all")
                        .param("cityFrom", cityFrom)
                        .param("cityTo", cityTo)
                        .param("dateStart", dateStart)
                        .param("dateFinish", dateFinish))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightService
                        .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable)
                        .map(flightMapper::convertToFlightDTOEntity))));
    }

}
