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
import org.springframework.web.bind.annotation.RestController;

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
        log.info("getAllTickets:");
        if (page == null || size == null) {
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllTickets: count: {}", tickets.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(tickets)));
        }
    }

    @Override
    public ResponseEntity<TicketDto> getTicketByNumber(String ticketNumber) {
        log.info("getTicketByNumber: by ticketNumber: {}", ticketNumber);
        var ticket = ticketService.getTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(ticketMapper.toDto(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TicketDto> createTicket(TicketDto ticketDto) {
        log.info("createTicket:");
        var savedTicket = ticketService.saveTicket(ticketDto);
        return new ResponseEntity<>(ticketMapper.toDto(savedTicket), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TicketDto> generatePaidTicket(Long bookingId) {
        log.info("generatePaidTicket: by bookingId: {}", bookingId);
        var savedTicket = ticketService.generatePaidTicket(bookingId);
        return new ResponseEntity<>(ticketMapper.toDto(savedTicket), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateTicket(Long id, TicketDto ticketDto) {
        log.info("updateTicket: by id: {}", id);
        return ResponseEntity.ok(ticketMapper.toDto(ticketService.updateTicketById(id, ticketDto)));
    }

    @Override
    public ResponseEntity<HttpStatus> deleteTicketById(Long id) {
        log.info("deleteTicketById: by id: {}", id);
        ticketService.deleteTicketById(id);
        return ResponseEntity.ok().build();
    }
}