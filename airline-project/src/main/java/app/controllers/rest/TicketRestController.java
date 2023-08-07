package app.controllers.rest;


import app.controllers.api.rest.TicketRestApi;
import app.dto.TicketDTO;
import app.services.interfaces.TicketService;
import app.util.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketRestController implements TicketRestApi {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Override
    public ResponseEntity<Page<TicketDTO>> getAllPagesTicketsDTO(Integer page, Integer size) {
        var ticketPage = ticketService.getAllTickets(page, size);
        if(ticketPage.isEmpty()){
            log.error("getAll: Tickets not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("getAll: get all Tickets");
        var ticketDTOS = ticketPage.stream().map(TicketDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(ticketDTOS, PageRequest.of(page, size), ticketPage.getTotalElements()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TicketDTO> getTicketDTOByTicketNumber(String ticketNumber) {
        log.info("getByNumber: Ticket by ticketNumber = {}", ticketNumber);
        var ticket = ticketService.getTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TicketDTO> createTicketDTO(TicketDTO ticketDTO) {
        log.info("create: new Ticket = {}", ticketDTO);
        var savedTicket = ticketService.saveTicket(ticketMapper.convertToTicketEntity(ticketDTO));
        return new ResponseEntity<>(new TicketDTO(savedTicket), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> updateTicketById(Long id, TicketDTO ticketDTO) {
        log.info("update: Ticket with id = {}", id);
        var ticket = ticketService.updateTicketById(id, ticketMapper.convertToTicketEntity(ticketDTO));
        return new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.OK);
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