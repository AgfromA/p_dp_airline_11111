package app.controllers;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.enums.CategoryType;
import app.services.interfaces.FlightSeatService;
import org.junit.jupiter.api.Test;
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

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-flightSeat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightSeatControllerIT extends IntegrationTestBase {

    @Autowired
    private FlightSeatService flightSeatService;

    @Test
    void shouldGetFlightSeats() throws Exception {
        var flightId = "1";
        var pageable = PageRequest.of(0, 10, Sort.by("id"));

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/all-flight-seats")
                        .param("flightId", flightId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(flightSeatService.getFlightSeatsByFlightId((Long.parseLong(flightId)), pageable).map(FlightSeatDTO::new))));
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
                        .writeValueAsString(flightSeatService.getFreeSeatsById(pageable, Long.parseLong(flightId)).map(FlightSeatDTO::new))));
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
                        .writeValueAsString(flightSeatService.getNotSoldFlightSeatsById((Long.parseLong(flightId)), pageable).map(FlightSeatDTO::new))));
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
        var flightId = "1";
        Set<FlightSeat> flightSeatSet = flightSeatService.getFlightSeatsByFlightId(1L);
        List<Long> idList = flightSeatSet.stream().map(FlightSeat::getId).collect(Collectors.toList());
        for (Long id : idList) {
            flightSeatService.deleteFlightSeatById(id);
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
        mockMvc.perform(patch("http://localhost:8080/api/flight-seats/{id}", id)
                        .content(objectMapper.writeValueAsString(new FlightSeatDTO(flightSeat)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void checkGetCheapestByFlightIdAndSeatCategory() throws Exception {
        var category = CategoryType.FIRST;
        Long flightID = 1L;
        List<FlightSeat> flightSeats = flightSeatService.getCheapestFlightSeatsByFlightIdAndSeatCategory(flightID, category);
        List<FlightSeatDTO> flightSeatDTOS = flightSeats.stream().map(FlightSeatDTO::new).collect(Collectors.toList());

        mockMvc.perform(get("http://localhost:8080/api/flight-seats/cheapest")
                        .param("category", category.toString())
                        .param("flightID", flightID.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(flightSeatDTOS)));
    }

}
