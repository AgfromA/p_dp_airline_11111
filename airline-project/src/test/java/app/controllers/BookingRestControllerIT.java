package app.controllers;

import app.dto.BookingDTO;
import app.enums.CategoryType;
import app.repositories.BookingRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.FlightService;
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
@Sql({"/sqlQuery/create-passengerAircraftDestinationFlightsCategoryBooking-before.sql"})
@Transactional
class BookingRestControllerIT extends IntegrationTestBase {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Save Booking")
    void shouldSaveBooking() throws Exception {
        var booking = new BookingDTO();
        booking.setBookingNumber("BK-111111");
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassengerId(passengerService.getPassengerById(1001L).get().getId());
        booking.setFlightId(flightService.getFlightById(4001L).get().getId());
        booking.setCategoryType(CategoryType.ECONOMY);

        mockMvc.perform(post("http://localhost:8080/api/bookings")
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Get All Bookings")
    void shouldGetAllBookings() throws Exception {
        var pageable = PageRequest.of(0, 1);
        mockMvc.perform(get("http://localhost:8080/api/bookings?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingService.getAllBookings(pageable.getPageNumber(), pageable.getPageSize()).map(BookingDTO::new))));
    }


    @Test
    @DisplayName("Get Booking by ID")
    void shouldGetBookingById() throws Exception {
        long id = 6001;
        mockMvc.perform(get("http://localhost:8080/api/bookings/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new BookingDTO(bookingService.getBookingById(id)))));
    }


    @Test
    @DisplayName("Get Booking by Number")
    void shouldGetBookingByNumber() throws Exception {
        var bookingNumber = "000000001";
        mockMvc.perform(get("http://localhost:8080/api/bookings/number")
                        .param("bookingNumber", bookingNumber))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new BookingDTO(bookingService
                        .getBookingByNumber(bookingNumber)))));
    }


    @Test
    @DisplayName("Edit Booking by ID")
    void shouldEditBookingById() throws Exception {
        long id = 6002;
        var booking = new BookingDTO(bookingService.getBookingById(id));
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassengerId(passengerService.getPassengerById(1002L).get().getId());
        booking.setFlightId(4002L);
        booking.setCategoryType(CategoryType.BUSINESS);
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