package app.controllers;

import app.exceptions.controller.responses.ErrorResponse;
import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.enums.Airport;
import app.exceptions.controller.SearchControllerException;
import app.services.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static app.enums.Airport.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Autowired
    private SearchService searchService;

    // Прямые Рейсы туда: Внуково-Омск
    // Прямые рейсы обратно: Омск-Внуково

    //1. В базе: один прямой рейс туда с наличием мест (3 свободных).
    //   Поиск: рейс туда (2023-04-01) без поиска обратного рейса для 2-х пассажиров
    @DisplayName("1 test. In DB 1 direct depart flight with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartFlight() throws Exception {
        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = null;
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expDirReturnFlights = 0;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
                        .param("airportFrom", String.valueOf(airportFrom))
                        .param("airportTo", String.valueOf(airportTo))
                        .param("departureDate", String.valueOf(departureDate))
                        .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))
                ))
                .andReturn().getResponse().getContentAsString();

        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());

        long dataToCount = searchResult.getFlights().stream().filter(flight -> flight.getDataTo() != null).count();
        long dataBackCount = searchResult.getFlights().stream().filter(flight -> flight.getDataBack() != null).count();

        assertEquals(expDirDepartFlights + expDirReturnFlights, dataBackCount + dataToCount);
        assertNull(searchResult.getFlights().get(0).getDataBack());

        assertNumberOfDepartDirectFlights(expDirDepartFlights, search, json);
        assertNumberOfReturnDirectFlights(expDirReturnFlights, search, json);
    }

    //2. В базе: один прямой рейс туда и один прямой рейс обратно с наличием мест (3 свободных мест).
    //   Поиск: рейс туда (2023-04-01) и рейс обратно (2023-04-03) для 2-х пассажиров
    @DisplayName("2 test. In DB 1 direct depart flight  1 direct return flight")
    @Test
    void shouldReturnOneDirectDepartAndOneDirectReturnFlights() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 3);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expDirReturnFlights = 1;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
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
                .andReturn().getResponse().getContentAsString();

        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());
        assertNotNull(searchResult.getFlights().get(0).getDataBack());
        assertNotNull(searchResult.getFlights().get(0).getDataTo());

        long dataToCount = searchResult.getFlights().stream().filter(flight -> flight.getDataTo() != null).count();
        long dataBackCount = searchResult.getFlights().stream().filter(flight -> flight.getDataBack() != null).count();

        assertEquals(expDirDepartFlights + expDirReturnFlights, dataBackCount + dataToCount);

        assertNumberOfDepartDirectFlights(expDirDepartFlights, search, json);
        assertNumberOfReturnDirectFlights(expDirReturnFlights, search, json);
    }

    //3. В базе: два прямых рейсов туда и два прямых рейсов обратно (туда и обратно - 3 свободных мест).
    //    Поиск: рейсы туда (2023-03-01) и рейсы обратно (2023-04-06) для 2-х пассажиров
    @DisplayName("3 test. In DB 2 direct depart flight with 3 free seats " +
            "and 2 direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 6);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 2;
        int expDirReturnFlights = 2;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
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
                .andReturn().getResponse().getContentAsString();

        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());

        long dataToCount = searchResult.getFlights().stream().filter(flight -> flight.getDataTo() != null).count();
        long dataBackCount = searchResult.getFlights().stream().filter(flight -> flight.getDataBack() != null).count();
        // деление на 2 из-за комбинаций полетов туда и обратно
        assertEquals(expDirDepartFlights + expDirReturnFlights, (dataToCount + dataBackCount) / 2);
        assertNumberOfReturnDirectFlights(expDirReturnFlights, search, json);
    }

    //4. В базе: два прямых туда и два прямых рейсов
    //   обратно, туда с наличием мест (3 свободных), обратно на первом (3 свободных), на втором (0).
    //   Поиск: рейс туда (2023-04-20), рейс обратно (2023-04-25) для 2-х пассажиров
    @DisplayName("4 test.In DB 2 direct depart flight with 3 free seats" +
            "and 2 direct return flight, one of them with 0 free seats")
    @Test
    void shouldReturnTwoDirectAndTwoNonDirectDepartAndTwoDirectAndTwoNonDirectReturnFlightsWithThreeSeatsOnEach()
            throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 20);
        LocalDate returnDate = LocalDate.of(2023, 4, 25);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 2;
        int expDirReturnFlights = 1;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
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
                .andReturn().getResponse().getContentAsString();

        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());

        long dataToCount = searchResult.getFlights().stream().filter(flight -> flight.getDataTo() != null).count();
        long dataBackCount = searchResult.getFlights().stream().filter(flight -> flight.getDataBack() != null).count();
        // деление на 2 из-за комбинаций полетов туда и обратно
        assertEquals(expDirDepartFlights + expDirReturnFlights, (dataToCount + dataBackCount) / 2);
        assertNotNull(searchResult.getFlights().get(0).getDataBack());

        assertNumberOfDepartDirectFlights(expDirDepartFlights, search, json);
        assertNumberOfReturnDirectFlights(expDirReturnFlights, search, json);
    }

    //5. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 3, обратно - 0 свободных мест).
    //    Поиск: рейс туда (2023-04-01) и рейс обратно (2023-05-03) для 2-х пассажиров
    @DisplayName("5 test. In DB 1 direct depart flight with 3 free seats and 1 direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectDepartFlightsWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 5, 3);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expDirReturnFlights = 0;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
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
                .andReturn().getResponse().getContentAsString();

        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());
        long dataToCount = searchResult.getFlights().stream().filter(flight -> flight.getDataTo() != null).count();
        long dataBackCount = searchResult.getFlights().stream().filter(flight -> flight.getDataBack() != null).count();

        assertEquals(expDirDepartFlights + expDirReturnFlights, dataBackCount + dataToCount);

        assertNotNull(searchResult.getFlights().get(0).getDataTo());
        assertNull(searchResult.getFlights().get(0).getDataBack());

        assertNumberOfDepartDirectFlights(expDirDepartFlights, search, json);
        assertNumberOfReturnDirectFlights(expDirReturnFlights, search, json);
    }

    private SearchResult getSearchResult(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SearchResult.class);
    }

    // Посчитать количество прямых флайтов туда
    private void assertNumberOfDepartDirectFlights(int expectDirect, Search search,
                                                   String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        // Посчитать количество прямых флайтов туда
        int numberOfDirectDepartFlights = 0;
        for (int i = 0; i < searchResult.getFlights().size(); i++) {
            if (searchResult.getFlights().get(i).getDataTo().getAirportFrom() == search.getFrom() &&
                    searchResult.getFlights().get(i).getDataTo().getAirportTo() == search.getTo()) {
                numberOfDirectDepartFlights++;
            }
        }
    }

    // Посчитать количество прямых обратно
    private void assertNumberOfReturnDirectFlights(int expectDirect, Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        int numberOfDirectReturnFlights = 0;
        Set<String> uniqueDataBack = new HashSet<>();

        for (int i = 0; i < searchResult.getFlights().size(); i++) {
            var flight = searchResult.getFlights().get(i);
            var dataBack = flight.getDataBack();

            if (dataBack != null && !uniqueDataBack.contains(dataBack.toString())) {
                if (dataBack.getAirportFrom() != null &&
                        dataBack.getAirportTo() != null &&
                        dataBack.getAirportFrom().equals(search.getTo()) &&
                        dataBack.getAirportTo().equals(search.getFrom())) {
                    numberOfDirectReturnFlights++;
                }
                uniqueDataBack.add(dataBack.toString());
            }
        }
        assertEquals(expectDirect, numberOfDirectReturnFlights);
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
