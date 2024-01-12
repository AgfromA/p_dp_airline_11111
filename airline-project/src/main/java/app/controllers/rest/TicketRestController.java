package app.controllers.rest;

import app.controllers.api.rest.TicketRestApi;
import app.dto.TicketDto;
import app.mappers.TicketMapper;
import app.services.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketRestController implements TicketRestApi {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Override
    public ResponseEntity<List<TicketDto>> getAllTickets(Integer page, Integer size) {
        log.info("getAll: get all Tickets");
        if (page == null || size == null) {
            log.info("getAll: get all List Tickets");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var ticketPage = ticketService.getAllTickets(page, size);

        return ticketPage.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(ticketPage.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<TicketDto>> createUnPagedResponse() {
        var tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            log.error("getAll: Tickets not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Tickets", tickets.size());
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<TicketDto> getTicketByTicketNumber(String ticketNumber) {
        log.info("getByNumber: Ticket by ticketNumber = {}", ticketNumber);
        var ticket = ticketService.getTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(ticketMapper.convertToTicketDto(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TicketDto> createTicket(TicketDto ticketDTO) {
        log.info("create: new Ticket = {}", ticketDTO);
        var savedTicket = ticketService.saveTicket(ticketDTO);
        return new ResponseEntity<>(ticketMapper.convertToTicketDto(savedTicket), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateTicketById(Long id, TicketDto ticketDTO) {
        log.info("update: Ticket with id = {}", id);
        var ticket = ticketService.updateTicketById(id, ticketDTO);
        return new ResponseEntity<>(ticketMapper.convertToTicketDto(ticket), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteTicketById(Long id) {
        try {
            ticketService.deleteTicketById(id);
            log.info("delete: Ticket. id={}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("delete: Ticket with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}