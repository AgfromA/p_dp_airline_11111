package app.controllers;

import app.dto.FlightDto;
import app.entities.Aircraft;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.repositories.*;
import app.services.FlightService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    private AircraftRepository aircraftRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FlightSeatRepository flightSeatRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private DestinationRepository destinationRepository;

    public Flight createFlight() {

        var aircraft = new Aircraft();
        aircraft.setId(Long.valueOf(1));
        aircraft.setAircraftNumber("ABC123");
        aircraft.setModel("Airbus");
        aircraft.setFlightRange(1000);
        aircraft.setModelYear(2012);

        var seat = new Seat();
        seat.setSeatNumber("1A");
        seat.setIsNearEmergencyExit(false);
        seat.setIsLockedBack(true);
        seat.setCategory(categoryRepository.findById(1L).get());
        seat.setAircraft(aircraft);

        var from = destinationRepository.getDestinationByAirportCode(Airport.OMS);
        var to = destinationRepository.getDestinationByAirportCode(Airport.VKO);

        var flight = new Flight();
        flight.setFrom(from);
        flight.setTo(to);
        flight.setCode("OMSVKO");
        flight.setFlightStatus(FlightStatus.COMPLETED);
        flight.setAircraft(aircraft);
        flight.setArrivalDateTime(LocalDateTime.parse("2024-01-21T01:00:00"));
        flight.setDepartureDateTime(LocalDateTime.parse("2024-01-21T03:00:00"));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setFare(1000);
        flightSeat.setIsBooked(false);
        flightSeat.setIsSold(false);
        flightSeat.setIsRegistered(false);
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);


        seatRepository.save(seat);
        aircraftRepository.save(aircraft);
        flightRepository.save(flight);
        flightSeatRepository.save(flightSeat);

        return flight;
    }

    @Nested
    class GetAllFlightsOrFlightsByParamsTest {

        @Test
        void shouldGetAllFlights() throws Exception {
            createFlight();
            mockMvc.perform(get("http://localhost:8080/api/flights"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetAllFlightsByNullPage() throws Exception {
            createFlight();
            mockMvc.perform(get("http://localhost:8080/api/flights?size=2"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetAllFlightsByNullSize() throws Exception {
            createFlight();
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void shouldGetBadRequestByPage() throws Exception {
            createFlight();
            mockMvc.perform(get("http://localhost:8080/api/flights?page=-1&size=2"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldGetBadRequestBySize() throws Exception {
            createFlight();
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldGetPageFlights() throws Exception {
            createFlight();
            var pageable = PageRequest.of(0, 4);
            mockMvc.perform(get("http://localhost:8080/api/flights?page=0&size=4"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(flightService
                            .getAllFlights(pageable.getPageNumber(), pageable.getPageSize()))));
        }

        @Test
        void showAllFlights_test() throws Exception {
            createFlight();
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
    }

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