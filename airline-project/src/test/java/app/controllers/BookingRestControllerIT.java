package app.controllers;


import app.dto.BookingDto;
import app.entities.Booking;
import app.entities.Ticket;
import app.enums.BookingStatus;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import app.repositories.TicketRepository;
import app.services.BookingService;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-booking-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookingRestControllerIT extends IntegrationTestBase {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BookingMapper bookingMapper;

    @Test
    @DisplayName("Получение всех бронирований")
    void shouldGetAllBooking() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Попытка получить Page на котором нет бронирований")
    void shouldGetBookingWhenBookingIsEmpty() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=10&size=5"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение всех бронирований, когда в запросе не указан Page")
    void shouldGetAllBookingByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение всех бронирований, когда в запросе не указан Size")
    void shouldGetAllBookingByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Введение некорректных данных в поле Page")
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Введение некорректных данных в поле Size")
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=1&size=-2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Запрос на получение всех бронирований с пустыми полями Page и Size")
    void shouldGetBookingWhenPageNullAndSizeNull() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Запрос на получение бронирований с пустым полем Page")
    void shouldGetPageBookingWithPageNullAndSizePresent() throws Exception {
        var pageable = PageRequest.of(0, 3);
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0&size=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(objectMapper.writeValueAsString(bookingService
                                .getAllBookings(pageable.getPageNumber(), pageable.getPageSize()))));
    }

    @Test
    @DisplayName("Создание бронирования места в самолете")
    void shouldSaveBooking() throws Exception {
        var bookingDto = new BookingDto();
        bookingDto.setFlightSeatId(5L);
        bookingDto.setPassengerId(1L);
        bookingDto.setFlightId(5L);
        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Попытка забронировать место в самолете, которое уже забронировано ранее")
    void shouldNotSaveBookingBecauseFlightSeatIsBooked() throws Exception {
        var bookingDto = new BookingDto();
        bookingDto.setFlightSeatId(4L);
        bookingDto.setFlightId(5L);
        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Попытка забронировать место в самолете, которое уже продано")
    void shouldNotSaveBookingBecauseFlightSeatIsSold() throws Exception {
        var bookingDto = new BookingDto();
        bookingDto.setFlightSeatId(3L);
        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Получение бронирования по ID")
    void shouldGetBookingById() throws Exception {
        long id = 1;
        var booking = bookingService.getBookingDto(id).get();
        var json = objectMapper.writeValueAsString(booking);
        mockMvc.perform(get("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("Попытка получения бронирования по несуществующему ID")
    void getNotExistedBooking() throws Exception {
        long id = 33;
        mockMvc.perform(get("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Изменение забронированного места в самолете")
    void shouldEditBooking() throws Exception {
        var id = 1L;
        var bookingDTO = bookingService.getBookingDto(id).get();
        bookingDTO.setFlightSeatId(5L);
        bookingDTO.setFlightId(5L);
        var numberOfBookingFlightSeat = bookingDTO.getFlightSeatId();

        mockMvc.perform(patch("http://localhost:8080/api/bookings/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(bookingRepository.count()
                        , equalTo(numberOfBookingFlightSeat)));
    }

    @Test
    @DisplayName("Попытка изменить несуществующую бронь")
    void editNotExistedBooking() throws Exception {
        long id = 100;
        long numberOfNotExistedBooking = bookingRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/booking/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingService.getBookingDto(100L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(bookingRepository.count(), equalTo(numberOfNotExistedBooking)));
    }

    @Test
    @DisplayName("Проверка валидации полей passenger_id и flightSeat_id")
    void shouldGetValidError() throws Exception {
        var bookingDto = new BookingDto();
        bookingDto.setPassengerId(0L);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        bookingDto.setFlightSeatId(0L);
        var id = bookingDto.getFlightSeatId();
        mockMvc.perform(patch("http://localhost:8080/api/seats/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Попытка удалить несуществующую бронь")
    void deleteNotExistedBooking() throws Exception {
        long id = 33L;
        long numberOfNotExistedBooking = bookingRepository.count();

        mockMvc.perform(delete("http://localhost:8080/api/booking/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingService.getBookingDto(id)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(bookingRepository.count(), equalTo(numberOfNotExistedBooking)));
    }

    @Test
    @DisplayName("Проверка удачного удаления бронирования")
    void successDeleteBooking() throws Exception {
        var bookingDto = bookingService.getBookingDto(1L).get();
        long id = bookingDto.getId();

        mockMvc.perform(delete("http://localhost:8080/api/bookings/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Попытка сохранить бронирование с пустыми полями")
    void shouldReturnBadRequestWhenFieldsAreNull() throws Exception {
        var bookingDTO = new BookingDto();

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка автоматической генерации id")
    void shouldNotAllowUserToSetId() throws Exception {

        var bookingDto = new BookingDto();
        bookingDto.setFlightId(5L);
        bookingDto.setFlightSeatId(5L);
        bookingDto.setPassengerId(1L);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Проверка отсутствия возможности изменить id пользователем при POST запросе")
    void shouldNotChangeIdByUserFromPostRequest() throws Exception {

        var bookingDto = new BookingDto();
        bookingDto.setId(16L);
        bookingDto.setFlightSeatId(5L);
        bookingDto.setPassengerId(1L);
        bookingDto.setFlightId(5L);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1000));
    }

    @Test
    @DisplayName("Проверка отсутствия возможности изменить id пользователем при PATCH запросе")
    void shouldNotChangeIdByUserFromPatchRequest() throws Exception {

        var bookingDto = new BookingDto();
        bookingDto.setId(16L);
        bookingDto.setFlightSeatId(5L);
        bookingDto.setPassengerId(1L);
        bookingDto.setFlightId(5L);
        var id = 1L;

        mockMvc.perform(patch("http://localhost:8080/api/bookings/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Проверка на невозможность перетирания полей NULL'ами при PATCH запросе")
    void shouldNotFieldWithNullFromPatchRequest() throws Exception {

        var bookingDto = new BookingDto();
        bookingDto.setFlightId(5L);
        bookingDto.setId(16L);
        bookingDto.setFlightSeatId(5L);
        bookingDto.setPassengerId(1L);
        bookingDto.setBookingStatus(null);
        bookingDto.setBookingDate(null);
        var id = 1L;

        mockMvc.perform(patch("http://localhost:8080/api/bookings/{id}", id)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightSeatId").exists())
                .andExpect(jsonPath("$.passengerId").exists())
                .andExpect(jsonPath("$.bookingStatus").exists())
                .andExpect(jsonPath("$.bookingDate").exists());
    }

    @Test
    @DisplayName("Проверка попытки сохранения бронирования без пользователя/ с несуществующим пользователем")
    void shouldSaveBookingWithPassengerEmptyOrPassengerIsNull() throws Exception {

        var bookingDto = new BookingDto();
        bookingDto.setFlightSeatId(5L);
        bookingDto.setFlightId(5L);
        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        var bookingDto2 = new BookingDto();
        bookingDto2.setFlightSeatId(5L);
        bookingDto2.setPassengerId(88L);
        bookingDto2.setFlightId(5L);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Создание билета при установке статуса бронирования в PAID")
    void shouldCreateTicketWhenBookingStatusIsPaid() throws Exception {
        var bookingDto = new BookingDto();
        bookingDto.setBookingStatus(BookingStatus.NOT_PAID);
        bookingDto.setFlightId(5L);
        bookingDto.setPassengerId(1L);
        bookingDto.setFlightSeatId(5L);

        MvcResult postResult = mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String postResponseContent = postResult.getResponse().getContentAsString();
        BookingDto createdBookingDto = objectMapper.readValue(postResponseContent, BookingDto.class);
        Long bookingId = createdBookingDto.getId();

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
        BookingDto bookingDto1 = bookingMapper.toDto(booking);
        bookingDto.setBookingStatus(BookingStatus.PAID);

        mockMvc.perform(patch("http://localhost:8080/api/bookings/{id}", bookingId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Ticket> savedTicket = ticketRepository.findByBookingId(bookingId);
        assertTrue(savedTicket.isPresent());
    }
}
