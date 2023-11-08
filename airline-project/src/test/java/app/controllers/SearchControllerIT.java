package app.controllers;

import app.dto.search.SearchResult;
import app.enums.Airport;
import app.services.interfaces.SearchService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static app.enums.Airport.*;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Autowired
    private SearchService searchService;


    //1. В базе: один прямой рейс туда с наличием мест (3 свободных).
//   Поиск: рейс туда (2023-04-01) без поиска обратного рейса для 2-х пассажиров
    @DisplayName("1 test. Search return one direct depart flight with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartFlightWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = null;
        Integer numberOfPassengers = 2;

        SearchResult result = searchService.search(
                airportFrom, airportTo, departureDate, returnDate, numberOfPassengers
        );

        System.out.println(objectMapper.writeValueAsString(
                searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)));


        mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))
                ))
                .andExpect(jsonPath("$.search.departureDate").value(departureDate.toString()))
                .andExpect(jsonPath("$.search.returnDate", nullValue()))
                .andExpect(jsonPath("$.search.numberOfPassengers").value(numberOfPassengers))
                .andExpect(jsonPath("$.departFlights.size()").value(1))
                .andExpect(jsonPath("$.departFlights.[0].airportFrom").value(airportFrom.toString()))
                .andExpect(jsonPath("$.departFlights.[0].airportTo").value(airportTo.toString()))
                .andExpect(jsonPath("$.departFlights.[0].seats.size()",
                        Matchers.greaterThanOrEqualTo(numberOfPassengers)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats.size()").value(3))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isRegistered == false)].isRegistered",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isSold == false)].isSold",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isBooked == false)].isBooked",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.returnFlights.size()").value(0));
    }


    //2. В базе: один прямой рейс туда и один прямой рейс обратно с наличием мест (3 свободных).
//   Поиск: рейс туда (2023-04-01) и рейс обратно (2023-04-03) для 2-х пассажиров
    @DisplayName("2 test. Search return one direct depart flight with 3 free seats and one direct return " +
            "flight with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartAndOneDirectReturnFlightsWithThreeSeatsOnEarch() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 04, 01);
        LocalDate returnDate = LocalDate.of(2023, 04, 03);
        Integer numberOfPassengers = 2;

        SearchResult result = searchService.search(
                airportFrom, airportTo, departureDate, returnDate, numberOfPassengers
        );

        mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("returnDate", String.valueOf(returnDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))
                ))
                .andExpect(jsonPath("$.search.departureDate").value(departureDate.toString()))
                .andExpect(jsonPath("$.search.returnDate").value(returnDate.toString()))
                .andExpect(jsonPath("$.search.numberOfPassengers").value(numberOfPassengers))
                .andExpect(jsonPath("$.departFlights.size()", Matchers.greaterThan(0)))
                .andExpect(jsonPath("$.departFlights.[0].airportFrom").value(airportFrom.toString()))
                .andExpect(jsonPath("$.departFlights.[0].airportTo").value(airportTo.toString()))
                .andExpect(jsonPath("$.departFlights.[0].seats.size()",
                        Matchers.greaterThanOrEqualTo(numberOfPassengers)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats.size()").value(3))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isRegistered == false)].isRegistered",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isSold == false)].isSold",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.departFlights.[0].seats[*].[?(@.isBooked == false)].isBooked",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.returnFlights.size()", Matchers.greaterThan(0)))
                .andExpect(jsonPath("$.returnFlights.[0].airportFrom").value(airportTo.toString()))
                .andExpect(jsonPath("$.returnFlights.[0].airportTo").value(airportFrom.toString()))
                .andExpect(jsonPath("$.returnFlights.[0].seats.size()",
                        Matchers.greaterThanOrEqualTo(numberOfPassengers)
                ))
                .andExpect(jsonPath("$.returnFlights.[0].seats.size()").value(3))
                .andExpect(jsonPath("$.returnFlights.[0].seats[*].[?(@.isRegistered == false)].isRegistered",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.returnFlights.[0].seats[*].[?(@.isSold == false)].isSold",
                        Matchers.contains(false, false, false)
                ))
                .andExpect(jsonPath("$.returnFlights.[0].seats[*].[?(@.isBooked == false)].isBooked",
                        Matchers.contains(false, false, false)
                ));
    }


}