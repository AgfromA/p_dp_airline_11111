package app.controllers;

import app.exceptions.controller.responses.ErrorResponse;
import app.enums.Airport;
import app.exceptions.controller.SearchControllerException;
import app.services.interfaces.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static app.enums.Airport.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Autowired
    private SearchService searchService;

    @Test
    void CheckSuccessfulSearch() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = LocalDate.of(2023, 04, 05);
        Integer numberOfPassengers = 2;

        mockMvc.perform(get("http://localhost:8080/api/search")
                        .param("airportFrom", String.valueOf(airportFrom))
                        .param("airportTo", String.valueOf(airportTo))
                        .param("departureDate", String.valueOf(departureDate))
                        .param("returnDate", String.valueOf(returnDate))
                        .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))));
    }

    @Test
    public void checkExceptionSearchControllerWhenDepartureDateAfterReturnDate() throws Exception {
        String errorMessage = "DepartureDate must be earlier then ReturnDate";

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = LocalDate.of(2023, 03, 05);
        Integer numberOfPassengers = 2;

        AtomicReference<String> requestUrl = new AtomicReference<>();
        mockMvc.perform(get("http://localhost:8080/api/search")
                        .param("airportFrom", String.valueOf(airportFrom))
                        .param("airportTo", String.valueOf(airportTo))
                        .param("departureDate", String.valueOf(departureDate))
                        .param("returnDate", String.valueOf(returnDate))
                        .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andDo(result -> requestUrl.set(result.getRequest().getRequestURL().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ErrorResponse(
                                new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST),
                                requestUrl.get()
                        )
                )));
    }

    @Test
    public void checkExceptionSearchControllerWhenNumberOfPassengersIsNegative() throws Exception {
        String errorMessage = "NumberOfPassengers is incorrect";

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = LocalDate.of(2023, 05, 05);
        Integer numberOfPassengers = -1;

        AtomicReference<String> requestUrl = new AtomicReference<>();
        mockMvc.perform(get("http://localhost:8080/api/search")
                        .param("airportFrom", String.valueOf(airportFrom))
                        .param("airportTo", String.valueOf(airportTo))
                        .param("departureDate", String.valueOf(departureDate))
                        .param("returnDate", String.valueOf(returnDate))
                        .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andDo(result -> requestUrl.set(result.getRequest().getRequestURL().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ErrorResponse(
                                new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST),
                                requestUrl.get()
                        )
                )));
    }

    @Test
    public void checkExceptionSearchControllerWhenAirportToIsNull() throws Exception {
        String errorMessage = "NumberOfPassengers is incorrect";

        Airport airportFrom = VKO;
        Airport airportTo = null;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = LocalDate.of(2023, 05, 05);
        Integer numberOfPassengers = -1;

        AtomicReference<String> requestUrl = new AtomicReference<>();
        mockMvc.perform(get("http://localhost:8080/api/search")
                        .param("airportFrom", String.valueOf(airportFrom))
//                        .param("airportTo", String.valueOf(airportTo))
                        .param("departureDate", String.valueOf(departureDate))
                        .param("returnDate", String.valueOf(returnDate))
                        .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andDo(result -> requestUrl.set(result.getRequest().getRequestURL().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new ErrorResponse(
                                new MissingServletRequestParameterException("airportTo", "Airport"),
                                requestUrl.get()
                        )
                )));

    }
}