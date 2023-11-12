package app.controllers;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.enums.Airport;
import app.services.interfaces.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static app.enums.Airport.*;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
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
    // Непрямые рейсы туда: Внуково-Курумоч - Курумоч-Омск, Внуково-Казань - Казань-Омск
    // Непрямые рейсы обратно: Омск-Курумоч - Курумоч-Внуково, Омск-Казань - Казань-Внуково


    //1. В базе: один прямой рейс туда с наличием мест (3 свободных).
    //   Поиск: рейс туда (2023-04-01) без поиска обратного рейса для 2-х пассажиров
    @DisplayName("1 test. In DB 1 direct depart flight with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartFlightWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = null;
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }

    //2. В базе: один прямой рейс туда и один прямой рейс обратно с наличием мест (3 свободных мест).
    //   Поиск: рейс туда (2023-04-01) и рейс обратно (2023-04-03) для 2-х пассажиров
    @DisplayName("2 test. In DB 1 direct depart flight with 3 free seats and 1 direct return " +
            "flight with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartAndOneDirectReturnFlightsWithThreeSeatsOnEarch() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 3);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }

    // 3. В базе: один прямой и один непрямой рейс туда с наличием мест (3 свободных).
    //    Поиск: рейс туда (2023-04-05) без поиска обратного рейса для 2-х пассажиров
    @DisplayName("3 test. In DB 1 direct and 1 non direct depart flight with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartFlightsWithThreeSeatsOnEarch() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = null;
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        var json = mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers))
                ))
                .andReturn().getResponse().getContentAsString();

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    // 4. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
    //    с наличием мест (3 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-06) для 2-х пассажиров
    @DisplayName("4 test. In DB 1 direct and 1 non direct depart flight " +
            "and 1 direct return flight. All with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartAndOneDirectReturnFlightsWithThreeSeatsOnEach() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 6);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    // 5. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс
    //    с наличием мест (3 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-07) для 2-х пассажиров
    @DisplayName("5 test. In DB 1 direct and 1 non direct depart flight" +
            "and 1 non direct return flight. All with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartAndOneNonDirectReturnFlightsWithThreeSeatsOnEach() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 7);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;   // один непрямой рейс не найдется без прямого

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //6. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой
    //   рейс обратно с наличием мест (3 свободных).
    //   Поиск: рейс туда (2023-04-05), рейс обратно (2023-04-08) для 2-х пассажиров
    @DisplayName("6 test. In DB 1 direct and 1 non direct depart flight" +
            "and 1 direct and 1 non direct return flight. All with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartAndOneDirectAndOneNonDirectReturnFlightsWithThreeSeatsOnEach()
            throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //7. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы,
    //   все - с наличием мест. Поиск: рейс туда (2023-04-01), рейс обратно (2023-04-08) для 2-х пассажиров
    @DisplayName("7 test. In DB 1 direct depart flight" +
            "and 1 direct and 1 non direct return flight. All with 3 free seats")
    @Test
    void shouldReturnOneDirectDepartAndOneDirectAndOneNonDirectReturnFlightsWithThreeSeatsOnEach() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //8. В базе: туда только один непрямой рейс, обратно - один прямой и один непрямой рейсы,
    //   все - с наличием мест. Поиск: рейс туда (2023-04-06), рейс обратно (2023-04-08)  для 2-х пассажиров
    @DisplayName("8 test. In DB 1 non direct depart flight" +
            "and 1 direct and 1 non direct return flight. All with 3 free seats")
    @Test
    void shouldReturnOneNonDirectDepartAndOneDirectAndOneNonDirectReturnFlightsWithThreeSeatsOnEach() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 6);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;     // один непрямой рейс не найдется без прямого
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //9. В базе: два прямых и два непрямых рейсов туда и два прямых и два непрямых рейсов
    //   обратно, все с наличием мест (3 свободных).
    //   Поиск: рейс туда (2023-04-20), рейс обратно (2023-04-25) для 2-х пассажиров
    @DisplayName("9 test.In DB 2 direct and 2 non direct depart flight" +
            "and 2 direct and 2 non direct return flight. All with 3 free seats")
    @Test
    void shouldReturnTwoDirectAndTwoNonDirectDepartAndTwoDirectAndTwoNonDirectReturnFlightsWithThreeSeatsOnEach()
            throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 20);
        LocalDate returnDate = LocalDate.of(2023, 4, 25);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 2;
        int expNonDirDepartFlights = 2;
        int expDirReturnFlights = 2;
        int expNonDirReturnFlights = 2;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    // ТЕСТИРОВАНИЕ ПРИ НАЛИЧИИ МЕСТ ТОЛЬКО В РЕЙСАХ ТУДА


    //10. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 3 , обратно - 0 свободных мест).
    //    Поиск: рейс туда (2023-04-01) и рейс обратно (2023-05-03) для 2-х пассажиров
    @DisplayName("10 test. In DB 1 direct depart flight with 3 free seats and 1 direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectDepartFlightsWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 5, 3);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //11. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
    //    (туда - 3 , обратно - 0 свободных мест).
    //    Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-06) для 2-х пассажиров
    @DisplayName("11 test. In DB 1 direct and 1 non direct depart flight with 3 free seats " +
            "and 1 direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartFlightsWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 5, 6);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //12. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс (туда - 3, обратно
    //    0 свободных мест). Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-07) для 2-х пассажиров
    @DisplayName("12 test. In DB 1 direct and 1 non direct depart flight with 3 free seats " +
            "and 1 non direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartFlightsWithThreeSeats2() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 5, 7);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //13. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой рейс обратно (туда - 3 ,
    //    обратно 0 свободных). Поиск: рейс туда (2023-04-05), рейс обратно (2023-05-08)) для 2-х пассажиров
    @DisplayName("13 test. In DB 1 direct and 1 non direct depart flight with 3 free seats " +
            "and 1 direct and 1 non direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectDepartFlightsWithThreeSeats3() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 5);
        LocalDate returnDate = LocalDate.of(2023, 5, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //14. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы (туда - 3 , обратно -
    //    0 свободных мест). Поиск: рейс туда (2023-04-01), рейс обратно (2023-05-08) для 2-х пассажиров
    @DisplayName("14 test. In DB 1 direct depart flight with 3 free seats " +
            "and 1 direct and 1 non direct return flight with 0 free seats")
    @Test
    void shouldReturnOneDirectDepartFlightsWithThreeSeats4() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 1);
        LocalDate returnDate = LocalDate.of(2023, 5, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 1;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //15. В базе: туда только один непрямой рейс, обратно - один прямой и один непрямой рейсы
    //    (туда - 3 , обратно - 0 свободных мест).
    //    Поиск: рейс туда (2023-04-06), рейс обратно (2023-05-08) для 2-х пассажиров
    @DisplayName("15 test. In DB 1 non direct depart flight with 3 free seats " +
            "and 1 direct and 1 non direct return flight with 0 free seats")
    @Test
    void shouldReturnNoContentStatus() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 6);
        LocalDate returnDate = LocalDate.of(2023, 5, 8);
        Integer numberOfPassengers = 2;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("returnDate", String.valueOf(returnDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    //16. В базе: два прямых и два непрямых рейсы туда и два прямых и два непрямых рейсы
    //    (туда - 3 , обратно 0 свободных). Поиск: рейс туда (2023-04-20), рейс обратно (2023-05-25) для 2-х пассажиров
    @DisplayName("16 test. In DB 2 direct and 2 non direct depart flight with 3 free seats " +
            "and 2 direct and 2 non direct return flight with 0 free seats")
    @Test
    void shouldReturnTwoDirectAndTwoNonDirectDepartFlightsWithThreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 4, 20);
        LocalDate returnDate = LocalDate.of(2023, 5, 25);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 2;
        int expNonDirDepartFlights = 2;
        int expDirReturnFlights = 0;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    // ТЕСТИРОВАНИЕ ПРИ НАЛИЧИИ МЕСТ ТОЛЬКО В ОБРАТНЫХ РЕЙСАХ

    //17. В базе: один прямой рейс туда и один прямой рейс обратно (туда - 0, обратно - 3 свободных мест).
    //    Поиск: рейс туда (2023-03-01) и рейс обратно (2023-04-03) для 2-х пассажиров
    @DisplayName("17 test. In DB 1 direct depart flight with 0 free seats " +
            "and 1 direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 3);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //18. В базе: один прямой и один непрямой рейс туда (туда - 0 свободных мест).
    //    Поиск: рейс туда (2023-03-05) без поиска обратного рейса
    @DisplayName("18 test. In DB 1 direct and 1 non direct depart flight with 0 free seats")
    @Test
    void shouldReturnNoContentStatus2() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 5);
        LocalDate returnDate = null;
        Integer numberOfPassengers = 2;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    //19. В базе: один прямой и один непрямой рейс туда и обратно только прямой рейс
    //    (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-06)
    @DisplayName("19 test. In DB 1 direct and 1 non direct depart flight with 0 free seats " +
            "and 1 direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectReturnFlightWithFreeSeats2() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 6);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //20. В базе: один прямой и один непрямой рейс туда и обратно только непрямой рейс
    //    (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-07)
    @DisplayName("20 test. In DB 1 direct and 1 non direct depart flight with 0 free seats " +
            "and 1 non direct return flight with 3 free seats")
    @Test
    void shouldReturnNoContentStatus3() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 7);
        Integer numberOfPassengers = 2;

        Search search = searchService.search(airportFrom, airportTo, departureDate, returnDate, numberOfPassengers)
                .getSearch();
        mockMvc.perform(get("http://localhost:8080/api/search")
                .param("airportFrom", String.valueOf(airportFrom))
                .param("airportTo", String.valueOf(airportTo))
                .param("departureDate", String.valueOf(departureDate))
                .param("returnDate", String.valueOf(returnDate))
                .param("numberOfPassengers", String.valueOf(numberOfPassengers)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    //21. В базе: один прямой и один непрямой рейс туда и один прямой и один непрямой рейс
    //    обратно (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-05), рейс обратно (2023-04-08))
    @DisplayName("21 test. In DB 1 direct and 1 non direct depart flight with 0 free seats " +
            "and 1 direct and 1 non direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 5);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }

    //22. В базе: только один прямой рейс туда, обратно - один прямой и один непрямой рейсы
    //    (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-01), рейс обратно (2023-04-08)
    @DisplayName("22 test. In DB 1 direct depart flight with 0 free seats " +
            "and 1 direct and 1 non direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectReturnFlightWithFreeSeats2() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 1);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //23. В базе: туда только один НЕпрямой рейс, обратно - один прямой и один непрямой рейсы
    //    (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-06), рейс обратно (2023-04-08)
    @DisplayName("23 test. In DB 1 non direct depart flight with 0 free seats " +
            "and 1 direct and 1 non direct return flight with 3 free seats")
    @Test
    void shouldReturnOneDirectAndOneNonDirectReturnFlightWithFreeSeats3() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 6);
        LocalDate returnDate = LocalDate.of(2023, 4, 8);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //24. В базе: два прямых и два непрямых рейсы туда и два прямых и два непрямых рейсы
    //    обратно (туда - 0, обратно - 3 свободных мест). Поиск: рейс туда (2023-03-20), рейс обратно (2023-04-25)
    @DisplayName("24 test. In DB 1 direct and 1 non direct depart flight with 0 free seats " +
            "and 2 direct and 2 non direct return flight with 3 free seats")
    @Test
    void shouldReturnTwoDirectAndTwoNonDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 3, 20);
        LocalDate returnDate = LocalDate.of(2023, 4, 25);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 0;
        int expDirReturnFlights = 2;
        int expNonDirReturnFlights = 2;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //25.В базе: один прямой и два непрямых рейсов туда (места: прямые - 0, непрямые 2 рейса со свободными 3-мя местами)
    //   и два прямых и два непрямых рейсов обратно (места: прямые - 2, непрямые 1 рейс со свободн местами (3 места)).
    //   Поиск: рейс туда (2023-07-01), рейс обратно (2023-07-05)
    @DisplayName("25 test. In DB 1 direct (0 free seats) and 2 non direct (2 flight 3 free seats) depart flight" +
            "and 2 direct (2 flight with 3 free seats) and 2 non direct (1 flight with 3 free seats) return flight")
    @Test
    void shouldReturnTwoNonDirectDepartAndTwoDirectAndOneNonDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 7, 1);
        LocalDate returnDate = LocalDate.of(2023, 7, 5);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 0;
        int expNonDirDepartFlights = 2;
        int expDirReturnFlights = 2;
        int expNonDirReturnFlights = 1;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    //26. В базе: два прямых и два непрямых рейсов туда (прямые - 2, непрямые 1 рейс со свободными местами (3 места))
    //    и два прямых и один непрямой рейсы обратно (прямые - 1, непрямые 0 рейсов со свободными местами (3 места)).
    //    Поиск: рейс туда (2023-07-10), рейс обратно (2023-07-15)
    @DisplayName("26 test. In DB 2 direct (2 with free seats) and 2 non direct (1 with free seats) depart flight" +
            "and 2 direct (1 flight with 3 free seats) and 1 non direct (0 flight with free seats) return flight")
    @Test
    void shouldReturnTwoDirectAndOneNonDirectDepartAndOneDirectReturnFlightWithFreeSeats() throws Exception {

        Airport airportFrom = VKO;
        Airport airportTo = OMS;
        LocalDate departureDate = LocalDate.of(2023, 7, 10);
        LocalDate returnDate = LocalDate.of(2023, 7, 15);
        Integer numberOfPassengers = 2;

        int expDirDepartFlights = 2;
        int expNonDirDepartFlights = 1;
        int expDirReturnFlights = 1;
        int expNonDirReturnFlights = 0;

        int expectDepartSize = expDirDepartFlights + expNonDirDepartFlights * 2;
        int expectReturnSize = expDirReturnFlights + expNonDirReturnFlights * 2;
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

        assertSearchAndDepartAndReturnFlightsSizes(expectDepartSize, expectReturnSize, search, json);
        assertNumberOfDepartDirectAndNonDirectFlights(expDirDepartFlights, expNonDirDepartFlights, search, json);
        assertDepartFlightsNumberOfFreeSeats(search, json);
        assertNumberOfReturnDirectAndNonDirectFlights(expDirReturnFlights, expNonDirReturnFlights, search, json);
        assertReturnFlightsNumberOfFreeSeats(search, json);
    }


    private SearchResult getSearchResult(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SearchResult.class);
    }

    // Оценка параметров поиска, общего количества рейсов туда и обратно (еоличество непрямых рейсов умножается на 2)
    private void assertSearchAndDepartAndReturnFlightsSizes(int expectDepartSize, int expectReturnSize,
                                                            Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        assertEquals(search, searchResult.getSearch());
        assertEquals(expectDepartSize, searchResult.getDepartFlights().size());
        assertEquals(expectReturnSize, searchResult.getReturnFlights().size());
    }


    // Посчитать количество прямых и НЕпрямых флайтов туда
    private void assertNumberOfDepartDirectAndNonDirectFlights(int expectDirect, int expectNonDirect,
                                                               Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        // Посчитать количество прямых флайтов туда
        int numberOfDirectDepartFlights = 0;
        for (int i = 0; i < searchResult.getDepartFlights().size(); i++) {
            if (searchResult.getDepartFlights().get(i).getAirportFrom() == search.getFrom() &&
                    searchResult.getDepartFlights().get(i).getAirportTo() == search.getTo()) {
                numberOfDirectDepartFlights++;
            }
        }
        assertEquals(expectDirect, numberOfDirectDepartFlights);
        // Посчитать количество НЕпрямых флайтов туда
        int numberOfNonDirectDepartFlights = 0;
        for (int i = numberOfDirectDepartFlights; i < searchResult.getDepartFlights().size() - 1; i++) {
            if (searchResult.getDepartFlights().get(i).getAirportFrom() == search.getFrom() &&
                    searchResult.getDepartFlights().get(i).getAirportTo()
                            == searchResult.getDepartFlights().get(i + 1).getAirportFrom() &&
                    searchResult.getDepartFlights().get(i + 1).getAirportTo() == search.getTo()) {
                numberOfNonDirectDepartFlights++;
            }
        }
        assertEquals(expectNonDirect, numberOfNonDirectDepartFlights);

    }

    // Посчитать количество прямых и НЕпрямых флайтов обратно
    private void assertNumberOfReturnDirectAndNonDirectFlights(int expectDirect, int expectNonDirect,
                                                               Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        // Посчитать количество прямых флайтов обратно
        int numberOfDirectReturnFlights = 0;
        for (int i = 0; i < searchResult.getReturnFlights().size(); i++) {
            if (searchResult.getReturnFlights().get(i).getAirportFrom() == search.getTo() &&
                    searchResult.getReturnFlights().get(i).getAirportTo() == search.getFrom()) {
                numberOfDirectReturnFlights++;
            }
        }
        assertEquals(expectDirect, numberOfDirectReturnFlights);
        // Посчитать количество НЕпрямых флайтов обратно
        int numberOfNonDirectReturnFlights = 0;
        for (int i = numberOfDirectReturnFlights; i < searchResult.getReturnFlights().size() - 1; i++) {
            if (searchResult.getReturnFlights().get(i).getAirportFrom() == search.getTo() &&
                    searchResult.getReturnFlights().get(i).getAirportTo()
                            == searchResult.getReturnFlights().get(i + 1).getAirportFrom() &&
                    searchResult.getReturnFlights().get(i + 1).getAirportTo() == search.getFrom()) {
                numberOfNonDirectReturnFlights++;
            }
        }
        assertEquals(expectNonDirect, numberOfNonDirectReturnFlights);
    }

    // Проверяем рейсы туда на количество свободных мест
    private void assertDepartFlightsNumberOfFreeSeats(Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        for (int i = 0; i < searchResult.getDepartFlights().size(); i++) {
            assertEquals(search.getDepartureDate(), searchResult.getDepartFlights().get(i).getDepartureDateTime().toLocalDate());
            int numberOfFreeSeats = 0;
            for (int j = 0; j < searchResult.getDepartFlights().get(i).getSeats().size(); j++) {
                if (!searchResult.getDepartFlights().get(i).getSeats().get(j).getIsBooked()
                        && !searchResult.getDepartFlights().get(i).getSeats().get(j).getIsSold() &&
                        !searchResult.getDepartFlights().get(i).getSeats().get(j).getIsRegistered()) {
                    numberOfFreeSeats++;
                }
            }
            assertTrue(numberOfFreeSeats >= search.getNumberOfPassengers());
        }
    }

    // Проверяем рейсы обратно на количество свободных мест
    private void assertReturnFlightsNumberOfFreeSeats(Search search, String json) throws JsonProcessingException {
        var searchResult = getSearchResult(json);
        for (int i = 0; i < searchResult.getReturnFlights().size(); i++) {
            assertEquals(search.getReturnDate(), searchResult.getReturnFlights().get(i).getDepartureDateTime().toLocalDate());
            int numberOfFreeSeats = 0;
            for (int j = 0; j < searchResult.getReturnFlights().get(i).getSeats().size(); j++) {
                if (!searchResult.getReturnFlights().get(i).getSeats().get(j).getIsBooked()
                        && !searchResult.getReturnFlights().get(i).getSeats().get(j).getIsSold() &&
                        !searchResult.getReturnFlights().get(i).getSeats().get(j).getIsRegistered()) {
                    numberOfFreeSeats++;
                }
            }
            assertTrue(numberOfFreeSeats >= search.getNumberOfPassengers());
        }
    }
}