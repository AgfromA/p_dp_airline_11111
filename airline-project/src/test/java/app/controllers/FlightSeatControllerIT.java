package app.controllers;

import app.dto.FlightDTO;
import app.dto.FlightSeatDTO;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.enums.Airport;
import app.enums.CategoryType;
import app.mappers.FlightMapper;
import app.mappers.FlightSeatMapper;
import app.repositories.DestinationRepository;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;


@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flightSeat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightSeatControllerIT extends IntegrationTestBase {

    @Autowired
    private FlightSeatService flightSeatService;
    @Autowired
    private FlightSeatRepository flightSeatRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private FlightService flightService;
    @Autowired
    SeatService seatService;

    @Test
    void shouldGetFlightSeats() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats")
                        .param("flightId", flightId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getFlightSeatsByFlightId((Long.parseLong(flightId)), pageable))));
    }

    @Test
    void shouldGetFreeSeats() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats")
                        .param("flightId", flightId)
                        .param("isSold", "false")
                        .param("isRegistered", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getFreeSeatsById(pageable, Long.parseLong(flightId)))));
    }

    @Test
    void shouldGetNonSoldFlightSeatsByFlightId() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats")
                        .param("flightId", flightId)
                        .param("isSold", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getNotSoldFlightSeatsById((Long.parseLong(flightId)), pageable))));
    }

    @Test
    void shouldReturnExistingFlightSeatsByFlightId() throws Exception {
        var flightId = "1";
        Set<FlightSeat> flightSeatSet = flightSeatService.addFlightSeatsByFlightNumber(flightId);
        mockMvc.perform(
                        post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                                .content(objectMapper.writeValueAsString(flightSeatSet))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddFlightSeatsByFlightId() throws Exception {
        Flight flight = flightRepository.findById(1L).get();
        Destination from = destinationRepository.getDestinationByAirportCode(Airport.VKO);
        Destination to = destinationRepository.getDestinationByAirportCode(Airport.OMS);
        flight.setFrom(from);
        flight.setTo(to);
        flight.setSeats(flightSeatService.findByFlightId(1L));
        FlightDTO flightDTO = Mappers.getMapper(FlightMapper.class).flightToFlightDTO(flight, flightService);
        flightService.updateFlight(1L, flightDTO);
        var flightId = "1";
        Set<FlightSeat> flightSeatSet = flightSeatService.getFlightSeatsByFlightId(1L);
        for (FlightSeat flightSeat : flightSeatSet) {
            System.out.println(flightSeat.getId());
            flightSeatService.deleteFlightSeatById(flightSeat.getId());
        }
        mockMvc.perform(
                        post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldEditFlightSeatById() throws Exception {
        Long id = (long) 2;
        var flightSeat = flightSeatService.getFlightSeatById(id).get();
        flightSeat.setFare(100);
        flightSeat.setIsSold(false);
        flightSeat.setIsRegistered(false);
        long numberOfFlightSeat = flightSeatRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/flight-seats/{id}", id)
                        .content(objectMapper.writeValueAsString(Mappers.getMapper(FlightSeatMapper.class).convertToFlightSeatDTOEntity(flightSeat, flightService)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(flightSeatRepository.count(), equalTo(numberOfFlightSeat)));
    }

    @Test
    void checkGetCheapestByFlightIdAndSeatCategory() throws Exception {
        var category = CategoryType.FIRST;
        Long flightID = 1L;
        List<FlightSeat> flightSeats = flightSeatService.getCheapestFlightSeatsByFlightIdAndSeatCategory(flightID, category);
        List<FlightSeatDTO> flightSeatDTOS = flightSeats.stream().map(f -> Mappers.getMapper(FlightSeatMapper.class).convertToFlightSeatDTOEntity(f,flightService)).collect(Collectors.toList());

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/cheapest")
                        .param("category", category.toString())
                        .param("flightID", flightID.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightSeatDTOS)));
    }

    @Test
    void shouldGenerateFlightSeatsForFlightIdempotent() throws Exception {
        String flightId = "1";
        mockMvc.perform(post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:8080/api/flight-seats/all-flight-seats/{flightId}", flightId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
