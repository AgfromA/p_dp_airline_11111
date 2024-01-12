package app.controllers;

import app.dto.FlightDto;
import app.dto.FlightSeatDto;
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
import app.services.FlightSeatService;
import app.services.FlightService;
import app.services.SeatService;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

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

    // Пагинация 2.0
    @Test
    void shouldGetAllFlightSeats() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllFlightSeatsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllFlightSeatsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPageFlightSeats() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightSeatService
                        .getAllFlightSeats(pageable.getPageNumber(), pageable.getPageSize())
                        .getContent())));
    }
    // Пагинация 2.0

    @Test
    void shouldGetFlightSeats() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0&size=10")
                        .param("flightId", flightId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getFlightSeatsByFlightId((Long.parseLong(flightId)), pageable).getContent())));
    }

    @Test
    void shouldGetFreeSeats() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0&size=10")
                        .param("flightId", flightId)
                        .param("isSold", "false")
                        .param("isRegistered", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getFreeSeatsById(pageable, Long.parseLong(flightId)).getContent())));
    }

    @Test
    void shouldGetNonSoldFlightSeatsByFlightId() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats?page=0&size=10")
                        .param("flightId", flightId)
                        .param("isSold", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getNotSoldFlightSeatsById((Long.parseLong(flightId)), pageable).getContent())));
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
        FlightDto flightDTO = Mappers.getMapper(FlightMapper.class).toDto(flight, flightService);
        flightService.updateFlight(1L, flightDTO);
        var flightId = "1";
        Set<FlightSeatDto> flightSeatSet = flightSeatService.getFlightSeatsByFlightId(1L);
        for (FlightSeatDto flightSeat : flightSeatSet) {
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
                        .content(objectMapper.writeValueAsString(Mappers.getMapper(FlightSeatMapper.class).toDto(flightSeat, flightService)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(flightSeatRepository.count(), equalTo(numberOfFlightSeat)));
    }

    @Test
    void checkGetCheapestByFlightIdAndSeatCategory() throws Exception {
        var category = CategoryType.FIRST;
        Long flightID = 1L;
        //List<FlightSeat> flightSeats = flightSeatService.getCheapestFlightSeatsByFlightIdAndSeatCategory(flightID, category);
        List<FlightSeatDto> flightSeatDtos = flightSeatService.getCheapestFlightSeatsByFlightIdAndSeatCategory(flightID, category);
        mockMvc.perform(get("http://localhost:8080/api/flight-seats/cheapest")
                        .param("category", category.toString())
                        .param("flightID", flightID.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightSeatDtos)));
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
