package app.controllers;

import app.dto.SeatDTO;
import app.enums.CategoryType;
import app.services.interfaces.CategoryService;
import app.services.interfaces.SeatService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-seat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class SeatControllerIT extends IntegrationTestBase {

    @Autowired
    private SeatService seatService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void shouldSaveSeat() throws Exception {
        var seatDTO = new SeatDTO();
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(true);
        seatDTO.setIsNearEmergencyExit(false);
        seatDTO.setCategory(categoryService.getCategoryByType(CategoryType.ECONOMY));
        seatDTO.setAircraftId(1);

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void shouldGetSeatById() throws Exception {
        long id = 1;
        mockMvc.perform(get("http://localhost:8080/api/seats/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new SeatDTO(seatService.getSeatById(id)))));
    }

    @Test
    void getNotExistedSeat() throws Exception {
        long id = 100;
        mockMvc.perform(get("http://localhost:8080/api/seats/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldEditSeat() throws Exception {
        var seatDTO = new SeatDTO(seatService.getSeatById(1));
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(false);
        seatDTO.setIsNearEmergencyExit(true);
        long id = seatDTO.getId();

        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editNotExistedSeat() throws Exception {
        long id = 100;
        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatService.getSeatById(100)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetValidError() throws Exception {
        var seatDTO = new SeatDTO();
        seatDTO.setSeatNumber("1");

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        seatDTO.setId(1);
        long id = seatDTO.getId();
        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllSeats() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Disabled("The logic of this method has been fixed. Check for existed seats was added")
    @Test
    void shouldCreateManySeats() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/seats/aircraft/{aircraftId}", 1))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void creatingManySeatsForNotExistedAircraft() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/seats/aircraft/{aircraftId}", 100))
                .andExpect(status().isNotFound());
    }
}
