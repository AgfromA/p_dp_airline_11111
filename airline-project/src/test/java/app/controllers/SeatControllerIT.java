package app.controllers;

import app.dto.AircraftDto;
import app.dto.SeatDto;
import app.enums.CategoryType;
import app.mappers.SeatMapper;
import app.repositories.SeatRepository;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;


@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-seat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SeatControllerIT extends IntegrationTestBase {

    @Autowired
    private SeatService seatService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private AircraftService aircraftService;

    // Пагинация 2.0
    @Test
    void shouldGetAllSeats() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllSeatsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllSeatsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/seats?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPageSeats() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/seats?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(seatService
                        .getAllPagesSeats(pageable.getPageNumber(), pageable.getPageSize())
                        .getContent())));
    }
    // Пагинация 2.0

    @Test
    void shouldSaveSeat() throws Exception {
        var seatDTO = new SeatDto();
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(true);
        seatDTO.setIsNearEmergencyExit(false);
        seatDTO.setCategory(CategoryType.ECONOMY);
        seatDTO.setAircraftId(1L);

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void shouldGetSeatById() throws Exception {
        long id = 1;
        mockMvc.perform(get("http://localhost:8080/api/seats/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(content().json(objectMapper.writeValueAsString(SeatMapper.INSTANCE.convertToSeatDtoEntity(seatService.getSeatById(id)))));
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
        var seatDTO = SeatMapper.INSTANCE.convertToSeatDtoEntity(seatService.getSeatById(1));
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(false);
        seatDTO.setIsNearEmergencyExit(true);
        long id = seatDTO.getId();
        long numberOfSeat = seatRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(seatRepository.count(), equalTo(numberOfSeat)));
    }

    @Test
    void editNotExistedSeat() throws Exception {
        long id = 100;
        long numberOfNotExistedSeat = seatRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatService.getSeatById(100)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(seatRepository.count(), equalTo(numberOfNotExistedSeat)));
    }

    @Test
    void shouldGetValidError() throws Exception {
        var seatDTO = new SeatDto();
        seatDTO.setSeatNumber("1");

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        seatDTO.setId(1L);
        long id = seatDTO.getId();
        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldCreateManySeats() throws Exception {
        var aircraft = new AircraftDto();
        aircraft.setAircraftNumber("17000010");
        aircraft.setModel("Airbus A319");
        aircraft.setModelYear(2002);
        aircraft.setFlightRange(3800);
        long aircraftId = aircraftService.saveAircraft(aircraft).getId();

        mockMvc.perform(post("http://localhost:8080/api/seats/aircraft/{aircraftId}", 1))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(post("http://localhost:8080/api/seats/aircraft/{aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void creatingManySeatsForNotExistedAircraft() throws Exception {
        mockMvc.perform(post("http://localhost:8080/api/seats/aircraft/{aircraftId}", 100))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetNotExistedSeatsDTOByAircraftId() throws Exception {
        var aircraftId = 100L;
        mockMvc.perform(get("http://localhost:8080/api/seats/aircraft/{aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetNotValidExistedSeatsDTOByAircraftId() throws Exception {
        var aircraftId = "notValid";
        mockMvc.perform(get("http://localhost:8080/api/seats/aircraft/{aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllPagesSeatsDTOByAircraftId() throws Exception {
        var aircraftId = 1L;
        var pageable = PageRequest.of(0, 30, Sort.by("id"));
        mockMvc.perform(get("http://localhost:8080/api/seats/aircraft/{aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(seatService.getPagesSeatsByAircraftId(aircraftId, pageable).getContent())));
    }
}
