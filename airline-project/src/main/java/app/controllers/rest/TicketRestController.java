package app.controllers.rest;

import app.controllers.api.rest.TicketRestApi;
import app.dto.TicketDto;
import app.mappers.TicketMapper;
import app.services.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        var examples = ticketService.getAllTickets(page, size);
        if (examples.isEmpty()) {
            return ResponseEntity.ok(Page.empty());
        } else {
            return ResponseEntity.ok(examples);
        }
    }

    private ResponseEntity<Page<TicketDto>> createUnPagedResponse() {
        var tickets = ticketService.getAllTickets();
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(tickets)));
        } else {
            log.info("getAllTickets: count: {}", tickets.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(tickets)));
        }
    }

    @Override
    public ResponseEntity<TicketDto> getTicketByNumber(String ticketNumber) {
        log.info("getTicketByNumber: by ticketNumber: {}", ticketNumber);
        return ticketService
                .getTicketByTicketNumber(ticketNumber)
                .map(value -> new ResponseEntity<>(ticketMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<InputStreamResource> getPdfByTicketNumber(String ticketNumber) throws FileNotFoundException {
        log.info("getPdfByTicketNumber: by ticketNumber: {}", ticketNumber);
        var pathToPdf = ticketService.getPathToTicketPdfByTicketNumber(ticketNumber);
        FileInputStream fileInputStream = new FileInputStream(pathToPdf);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=S7-Ticket.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileInputStream));
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