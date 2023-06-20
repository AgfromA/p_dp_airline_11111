package app.controllers.rest;


import app.controllers.api.rest.TicketRestApi;
import app.dto.TicketDTO;
import app.entities.Ticket;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TicketRestController implements TicketRestApi {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @Override
    public ResponseEntity<Page<TicketDTO>> getAll(Integer page, Integer size) {
        Page<Ticket> ticketPage = ticketService.findAll(page, size);
        if(ticketPage.isEmpty()){
            log.error("getAll: Tickets not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("getAll: get all Tickets");
        List<TicketDTO> ticketDTOS = ticketPage.stream().map(TicketDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(ticketDTOS, PageRequest.of(page, size), ticketPage.getTotalElements()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TicketDTO> getByNumber(String ticketNumber) {
        log.info("getByNumber: Ticket by ticketNumber = {}", ticketNumber);
        Ticket ticket = ticketService.findTicketByTicketNumber(ticketNumber);
        return ticket != null
                ? new ResponseEntity<>(new TicketDTO(ticket), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<TicketDTO> create(TicketDTO ticketDTO) {
        log.info("create: new Ticket = {}", ticketDTO);
        ticketService.saveTicket(ticketMapper.convertToTicketEntity(ticketDTO));
        return new ResponseEntity<>(ticketDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Long id, TicketDTO ticketDTO) {
        log.info("update: Ticket with id = {}", id);
        ticketDTO.setId(id);
        Ticket ticket = ticketService.updateTicket(id, ticketMapper.convertToTicketEntity(ticketDTO));
        return new ResponseEntity<>(new TicketDTO(ticket),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> delete(Long id) {
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