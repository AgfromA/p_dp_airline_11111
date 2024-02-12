package app.controllers;

import app.dto.TicketDto;
import app.entities.Booking;
import app.mappers.TicketMapper;
import app.repositories.TicketRepository;
import app.services.BookingService;
import app.services.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static app.enums.Airport.OMS;
import static app.enums.Airport.VKO;
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
@Sql(value = {"/sqlQuery/create-ticket-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TicketRestControllerIT extends IntegrationTestBase {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private TicketMapper ticketMapper;

    // Пагинация 2.0
    @Test
    void shouldGetAllTickets() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllTicketsByNullPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets?size=2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAllTicketsByNullSize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets?page=0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBadRequestByPage() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets?page=-1&size=2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBadRequestBySize() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets?page=0&size=0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPageTickets() throws Exception {
        var pageable = PageRequest.of(0, 4);
        mockMvc.perform(get("http://localhost:8080/api/tickets?page=0&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketService
                        .getAllTickets(pageable.getPageNumber(), pageable.getPageSize()))));
    }

    @DisplayName("createTicket(), successfully creates ticket and checks automatic id and ticketNumber generation")
    @Test
    void shouldCreateTicket() throws Exception {
        var newTicket = new TicketDto();
        newTicket.setFlightSeatId(1L);
        newTicket.setPassengerId(1L);
        newTicket.setBookingId(1L);
        newTicket.setFirstName("John1");
        newTicket.setLastName("Simons1");
        newTicket.setFrom(VKO);
        newTicket.setTo(OMS);
        newTicket.setCode("VKOOMS");
        newTicket.setSeatNumber("1A");
        newTicket.setArrivalDateTime(LocalDateTime.of(2023, 04, 01, 11, 20, 00));
        newTicket.setDepartureDateTime(LocalDateTime.of(2023, 04, 01, 17, 50, 00));
        newTicket.setId(null);
        mockMvc.perform(post("http://localhost:8080/api/tickets")
                        .content(objectMapper.writeValueAsString(newTicket))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ticketNumber").isNotEmpty());
    }

    @DisplayName("createPaidTicket(), successfully creates ticket by booking id")
    @Test
    void shouldCreatePaidTicket() throws Exception {
        var bookingDto = bookingService.getBookingDto(2L).get();
        mockMvc.perform(post("http://localhost:8080/api/tickets/{id}", bookingDto.getId())
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ticketNumber").isNotEmpty());
    }

    @Test
        // fixme тест написан неправильно
    void showTicketByBookingNumber_test() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/tickets/")
                        .param("ticketNumber", "SD-2222"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateTicket_test() throws Exception {
        var ticketDTO = ticketMapper.toDto(ticketService.getTicketByTicketNumber("ZX-3333"));
        ticketDTO.setTicketNumber("ZX-2222");
        long numberOfTicket = ticketRepository.count();

        mockMvc.perform(patch("http://localhost:8080/api/tickets/{id}", ticketDTO.getId())
                        .content(
                                objectMapper.writeValueAsString(ticketDTO)
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(result -> assertThat(ticketRepository.count(), equalTo(numberOfTicket)));
    }

    @Test
    void deleteTicket_test() throws Exception {
        Long id = 2L;
        mockMvc.perform(delete("http://localhost:8080/api/tickets/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
