package app.controllers;

import app.enums.Airport;
import app.services.interfaces.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static app.enums.Airport.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Autowired
    private SearchService searchService;

    @Test
    void CheckSearchResult() throws Exception {

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
                        .writeValueAsString(searchService.getSearch(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))));
    }
}
