package app.controllers.rest;

import app.controllers.api.rest.TicketRestApi;
import app.dto.TicketDto;
import app.mappers.TicketMapper;
import app.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class TicketRestController implements TicketRestApi {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Override
    public ResponseEntity<Page<TicketDto>> getAllTickets(Integer page, Integer size) {
        log.info("getAll: get all Tickets");
        if (page == null || size == null) {
            log.info("getAll: get all List Tickets");
            return createUnPagedResponse();
        }

        var tickets = ticketService.getAllTickets(page, size);
        return tickets.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    private ResponseEntity<Page<TicketDto>> createUnPagedResponse() {
        var tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            log.error("getAll: Tickets not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found: {} Tickets", tickets.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(tickets)));
        }
    }

    @Override
    public ResponseEntity<TicketDto> getTicketByTicketNumber(String ticketNumber) {
        log.info("getByNumber: Ticket by ticketNumber: {}", ticketNumber);
        var ticket = ticketService.getTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(ticketMapper.toDto(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketDto ticketDTO) {
        log.info("create: new Ticket: {}", ticketDTO);
        var savedTicket = ticketService.saveTicket(ticketDTO);
        return new ResponseEntity<>(ticketMapper.toDto(savedTicket), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateTicketById(Long id,@Valid @RequestBody TicketDto ticketDTO) {
        log.info("update: Ticket with id: {}", id);
        var ticket = ticketService.updateTicketById(id, ticketDTO);
        return new ResponseEntity<>(ticketMapper.toDto(ticket), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteTicketById(Long id) {
        try {
            ticketService.deleteTicketById(id);
            log.info("delete: Ticket. id: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("delete: Ticket with id: {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}