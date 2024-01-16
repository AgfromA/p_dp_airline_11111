package app.controllers;

import app.dto.FlightDto;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.FlightService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flight-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightRestControllerIT extends IntegrationTestBase {

    @Autowired
    private FlightService flightService;

    @Nested
    class GetAllFlightsOrFlightsByParamsTest {

        // Пагинация 2.0
        @Test
        void shouldGetAllFlights() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetAllFlightsByNullPage() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights?size=2"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetAllFlightsByNullSize() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetBadRequestByPage() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights?page=-1&size=2"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldGetBadRequestBySize() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldGetPageFlights() throws Exception {
            var pageable = PageRequest.of(0, 4);
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=4"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlights(pageable)
                            .getContent())));
        }
        // Пагинация 2.0

        @Test
        void showAllFlights_test() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @Sql(value = {"/sqlQuery/delete-from-tables.sql"})
        void showAllFlights_testError() throws Exception {
            mockMvc.perform(get("http://localhost:8080/api/flights")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is(204));
        }

        @Test
        void showFlightsByCityFrom() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String cityFrom = "Волгоград";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("cityFrom", cityFrom))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(cityFrom, null, null, null, pageable).getContent())));
        }

        @Test
        void showFlightsByCityTo() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String cityTo = "Омск";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("cityTo", cityTo))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(null, cityTo, null, null, pageable).getContent())));
        }

        @Test
        void showFlightsByDateStart() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String dateStart = "2022-11-23T04:30:00";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("dateStart", dateStart))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(null, null, dateStart, null, pageable).getContent())));
        }

        @Test
        void showFlightsByDateFinish() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String dateFinish = "2022-11-23T07:30:00";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("dateFinish", dateFinish))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(null, null, null, dateFinish, pageable).getContent())));
        }

        @Test
        void showFlightsByCityFromAndCityTo() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String cityFrom = "Волгоград";
            String cityTo = "Омск";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("cityFrom", cityFrom)
                            .param("cityTo", cityTo))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, null, null, pageable).getContent())));
        }

        @Test
        void showFlightsByDateStartAndDateFinish() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String dateStart = "2022-11-23T04:30:00";
            String dateFinish = "2022-11-23T07:30:00";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("dateStart", dateStart)
                            .param("dateFinish", dateFinish))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(null, null, dateStart, dateFinish, pageable).getContent())));
        }

        @Test
        void showFlightsByCityFromAndDateStart() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String cityFrom = "Волгоград";
            String dateStart = "2022-11-23T04:30:00";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("cityFrom", cityFrom)
                            .param("dateStart", dateStart))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(cityFrom, null, dateStart, null, pageable).getContent())));
        }

        @Test
        void showFlightsByAllParams() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
            String cityFrom = "Волгоград";
            String cityTo = "Омск";
            String dateStart = "2022-11-23T04:30:00";
            String dateFinish = "2022-11-23T07:30:00";
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=10")
                            .param("cityFrom", cityFrom)
                            .param("cityTo", cityTo)
                            .param("dateStart", dateStart)
                            .param("dateFinish", dateFinish))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable).getContent())));
        }
    }

    @Nested
    class EditFlightTest {
        @Test
        void contextLoads() throws Exception {
            assertThat(flightService).isNotNull();
        }

        @Test
        void shouldUpdateFlightById() throws Exception {
            String code = "MQFOMS";

            Airport airportFrom = Airport.MQF;

            Airport airportTo = Airport.OMS;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            LocalDateTime departureDateTime = LocalDateTime.of(2023, 11, 20, 8, 15, 0);
            String expectedDepartureFormattedDateTime = departureDateTime.format(formatter);

            LocalDateTime arrivalDateTime = LocalDateTime.of(2023, 11, 20, 13, 52, 0);
            String expectedArrivalFormattedDateTime = arrivalDateTime.format(formatter);

            Long aircraftId = 1L;

            FlightStatus flightStatus = FlightStatus.CANCELED;


            FlightDto flightDTO = new FlightDto();
            flightDTO.setCode(code);
            flightDTO.setAirportFrom(airportFrom);
            flightDTO.setAirportTo(airportTo);
            flightDTO.setDepartureDateTime(departureDateTime);
            flightDTO.setArrivalDateTime(arrivalDateTime);
            flightDTO.setAircraftId(aircraftId);
            flightDTO.setFlightStatus(flightStatus);

            mockMvc.perform(patch("http://localhost:8080/api/flights/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(flightDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.code").exists())
                    .andExpect(jsonPath("$.code").value(code))
                    .andExpect(jsonPath("$.airportFrom").exists())
                    .andExpect(jsonPath("$.airportFrom").value(airportFrom.toString()))
                    .andExpect(jsonPath("$.airportTo").exists())
                    .andExpect(jsonPath("$.airportTo").value(airportTo.toString()))
                    .andExpect(jsonPath("$.departureDateTime").exists())
                    .andExpect(jsonPath("$.departureDateTime").value(expectedDepartureFormattedDateTime))
                    .andExpect(jsonPath("$.arrivalDateTime").exists())
                    .andExpect(jsonPath("$.arrivalDateTime").value(expectedArrivalFormattedDateTime))
                    .andExpect(jsonPath("$.aircraftId").exists())
                    .andExpect(jsonPath("$.aircraftId").value(aircraftId.toString()))
                    .andExpect(jsonPath("$.flightStatus").exists())
                    .andExpect(jsonPath("$.flightStatus").value(flightStatus.toString()));
        }

        @Test
        void shouldNotUpdateFlightIfIdNotExist() throws Exception {
            FlightDto flightDTO = new FlightDto();
            flightDTO.setCode("VKOVOG");
            flightDTO.setAirportTo(Airport.VKO);
            flightDTO.setAirportFrom(Airport.VOG);
            flightDTO.setArrivalDateTime(LocalDateTime.of(2023, 10, 23, 10, 50, 0));
            flightDTO.setDepartureDateTime(LocalDateTime.of(2023, 10, 23, 8, 15, 0));
            flightDTO.setAircraftId(1L);
            flightDTO.setFlightStatus(FlightStatus.DELAYED);

            mockMvc.perform(patch("http://localhost:8080/api/flights/100")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(flightDTO)))
                    .andExpect(status().isNotFound());
        }
    }
}
