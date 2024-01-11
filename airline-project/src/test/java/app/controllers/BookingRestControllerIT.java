package app.controllers;

import app.dto.BookingDTO;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.PassengerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;


@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql({"/sqlQuery/create-flightSeat-before.sql"})
@Sql({"/sqlQuery/create-passengerAircraftDestinationFlightsCategoryBooking-before.sql"})
@Transactional
class BookingRestControllerIT extends IntegrationTestBase {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingMapper bookingMapper;

    // Пагинация 2.0
    @Test
    void shouldGetAllBookings() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllBookingsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllBookingsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get All Bookings")
    void shouldGetPageBookings() throws Exception {
        var pageable = PageRequest.of(0, 1);
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingService
                        .getAllBookings(pageable.getPageNumber(), pageable.getPageSize())
                        .getContent())));
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    // Пагинация 2.0

    @Test
    @DisplayName("Save Booking")
    void shouldSaveBooking() throws Exception {
        var booking = new BookingDTO();
        booking.setBookingNumber("BK-111111");
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassengerId(passengerService.getPassengerById(1001L).get().getId());
        booking.setFlightSeatId(1L);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Get Booking by ID")
    void shouldGetBookingById() throws Exception {
        long id = 6001;
        mockMvc.perform(get("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        bookingService.getBookingById(id))));
    }


    @Test
    @DisplayName("Get Booking by Number")
    void shouldGetBookingByNumber() throws Exception {
        var bookingNumber = "000000001";
        mockMvc.perform(get("http://localhost:8080/api/bookings/number")
                        .param("bookingNumber", bookingNumber))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        bookingService.getBookingByNumber(bookingNumber))));
    }


    @Test
    @DisplayName("Edit Booking by ID")
    void shouldEditBookingById() throws Exception {
        long id = 6002;
        var booking = bookingService.getBookingById(id);
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassengerId(passengerService.getPassengerById(1002L).get().getId());
        long numberOfBooking = bookingRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/bookings/{id}", id)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(booking)))
                .andExpect(result -> assertThat(bookingRepository.count(), equalTo(numberOfBooking)));
    }


    @Test
    @DisplayName("Delete Booking by ID")
    void shouldDeleteById() throws Exception {
        long id = 6003;
        mockMvc.perform(delete("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
        mockMvc.perform(get("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}