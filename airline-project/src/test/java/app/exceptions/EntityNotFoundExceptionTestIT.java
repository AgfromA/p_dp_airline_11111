package app.exceptions;

import app.controllers.IntegrationTestBase;
import app.mappers.TicketMapper;
import app.services.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-ticket-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EntityNotFoundExceptionTestIT extends IntegrationTestBase {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketMapper ticketMapper;

    @Test
    void shouldThrowExceptionIfPassengerIdNotExistWhenCreatedTicket() throws Exception {
        var newTicket = ticketService.getTicketByTicketNumber("ZX-3333");
        newTicket.setTicketNumber("SJ-9346");
        var ticketDTO = ticketMapper.convertToTicketDto(newTicket);
        ticketDTO.setPassengerId(0L);
        mockMvc.perform(post("http://localhost:8080/api/tickets")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionIfFlightIdNotExistWhenCreatedTicket() throws Exception {
        var newTicket = ticketService.getTicketByTicketNumber("ZX-3333");
        newTicket.setTicketNumber("SJ-9346");
        var ticketDTO = ticketMapper.convertToTicketDto(newTicket);
        ticketDTO.setFlightId(0L);
        mockMvc.perform(post("http://localhost:8080/api/tickets")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowExceptionIfIFlightSeatIdNotExistWhenCreatedTicket() throws Exception {
        var newTicket = ticketService.getTicketByTicketNumber("ZX-3333");
        newTicket.setTicketNumber("SJ-9346");
        var ticketDTO = ticketMapper.convertToTicketDto(newTicket);
        ticketDTO.setFlightSeatId(0L);
        mockMvc.perform(post("http://localhost:8080/api/tickets")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
