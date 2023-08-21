package app.controllers;

import app.dto.TicketDTO;
import app.entities.Ticket;
import app.repositories.TicketRepository;
import app.services.interfaces.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
@Sql(value = {"/sqlQuery/create-ticket-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TicketRestControllerIT extends IntegrationTestBase {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void createTicket_test() throws Exception {
        Ticket newTicket = ticketService.getTicketByTicketNumber("ZX-3333");
        newTicket.setTicketNumber("SJ-9346");
        newTicket.setId(null);
        var ticketDTO = new TicketDTO(newTicket);
        mockMvc.perform(post("http://localhost:8080/api/tickets")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(status().isCreated());
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
        var ticketDTO = new TicketDTO(ticketService.getTicketByTicketNumber("ZX-3333"));
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
