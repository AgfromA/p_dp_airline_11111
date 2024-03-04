package app.controllers;

import app.dto.AircraftDto;
import app.dto.SeatDto;
import app.enums.CategoryType;
import app.mappers.SeatMapper;
import app.repositories.SeatRepository;
import app.services.AircraftService;
import app.services.CategoryService;
import app.services.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    private SeatMapper seatMapper;

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
                        .getAllSeats(pageable.getPageNumber(), pageable.getPageSize()))));
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
                .andExpect(status().isCreated());

    }

    @Test
    void shouldGetSeatById() throws Exception {
        long id = 1;
        mockMvc.perform(get("http://localhost:8080/api/seats/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(content().json(objectMapper.writeValueAsString(seatMapper.toDto(seatService.getSeat(id)))));
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
        var seatDTO = seatMapper.toDto(seatService.getSeat(1));
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
                        .content(objectMapper.writeValueAsString(seatService.getSeat(100)))
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
        long aircraftId = aircraftService.createAircraft(aircraft).getId();

        mockMvc.perform(post("http://localhost:8080/api/seats/generate").param("aircraftId", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
        mockMvc.perform(post("http://localhost:8080/api/seats/generate").param("aircraftId", "1"))
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
        mockMvc.perform(get("http://localhost:8080/api/seats?page=0&size=4&aircraftId={aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllPagesSeatsDTOByAircraftId() throws Exception {
        var aircraftId = 1L;
        mockMvc.perform(get("http://localhost:8080/api/seats?page=0&size=30&aircraftId={aircraftId}", aircraftId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper
                        .writeValueAsString(seatService.getAllSeatsByAircraftId(0, 30, aircraftId))));
    }

    // Тест на попытку удаления несуществующего места
    @Test
    void deleteNotExistedSeat() throws Exception {
        long id = 1488;
        long numberOfNotExistedSeat = seatRepository.count();

        mockMvc.perform(delete("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatService.getSeat(1488)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(seatRepository.count(), equalTo(numberOfNotExistedSeat)));
    }

    // Тест на проверку успешного удаления места
    @Test
    void successfullDeleteSeat() throws Exception {

        var seatDTO = seatMapper.toDto(seatService.getSeat(1));
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(false);
        seatDTO.setIsNearEmergencyExit(true);
        long id = seatDTO.getId();

        mockMvc.perform(delete("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // Тест на проверку невозможности создания нового места с полями, равными null
    @Test
    void shouldReturnBadRequestWhenFieldsAreNull() throws Exception {
        var seatDTO = new SeatDto();

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // Тест на проверку автоматической генерации id нового пользователя
    @Test
    void shouldNotAllowUserToSetId() throws Exception {

        var seatDTO = new SeatDto();
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(true);
        seatDTO.setIsNearEmergencyExit(false);
        seatDTO.setCategory(CategoryType.ECONOMY);
        seatDTO.setAircraftId(1L);

        // Не устанавливаем id, так как он должен генерироваться приложением автоматически

        mockMvc.perform(post("http://localhost:8080/api/seats")
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());    // Проверяем, что json содержит id,
        //    хотя мы его не устанавливали
    }

    // Тест для проверки получения мест при некорректных данных
    @Test
    void shouldGetAllSeatsWithPagination() throws Exception {
        // Задаем значения для параметров page и size
        int page = 200;
        int size = 5;

        // Выполняем GET-запрос
        mockMvc.perform(get("http://localhost:8080/api/seats")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNoContent());
    }

    // Тест на проверку невозможности показать несуществующее место
    @Test
    void shouldGetNotExistSeatById() throws Exception {
        long id = 1488;
        mockMvc.perform(get("http://localhost:8080/api/seats/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @Test
    @DisplayName("Проверка отсутствия возможности у пользователя изменить id места в самолете при POST запросе")
    void shouldNotChangeIdByUserFromPostRequest() throws Exception {
        var seatDTO = new SeatDto();
        seatDTO.setId(33L);
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(not(33)));
    }

    @Test
    @DisplayName("Проверка отсутствия возможности у пользователя изменить id пассажира при PATCH запросе")
    void shouldNotChangeIdByUserFromPatchRequest() throws Exception {
        var seatDTO = new SeatDto();
        seatDTO.setId(33L);
        seatDTO.setSeatNumber("1B");
        seatDTO.setIsLockedBack(true);
        seatDTO.setIsNearEmergencyExit(false);
        seatDTO.setCategory(CategoryType.ECONOMY);
        seatDTO.setAircraftId(1L);

        var id = 1L;

        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(seatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.id").value(not(33)));
    }

}
