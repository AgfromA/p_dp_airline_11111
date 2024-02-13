package app.controllers.api.rest;

import app.dto.TicketDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = "Ticket REST")
@Tag(name = "Ticket REST", description = "API для операций с билетами")
@RequestMapping("/api/tickets")
public interface TicketRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Tickets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tickets found"),
            @ApiResponse(code = 204, message = "Tickets not found")
    })
    ResponseEntity<Page<TicketDto>> getAllTickets(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @ApiOperation(value = "Get Ticket by ticketNumber")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Found the ticket"),
            @ApiResponse(code = 404, message = "Ticket not found")
    })
    @GetMapping("/{ticketNumber}")
    ResponseEntity<TicketDto> getTicketByNumber(
            @ApiParam(
                    name = "ticketNumber",
                    value = "ticketNumber",
                    example = "SD-2222"
            )
            @PathVariable("ticketNumber") String ticketNumber);

    @ApiOperation(value = "Create new Ticket")
    @ApiResponse(code = 201, message = "Ticket created")
    @PostMapping
    ResponseEntity<TicketDto> createTicket(
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDto ticketDto);

    @ApiOperation(value = "Generate new Paid Ticket by existing paid Booking")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Paid Ticket created"),
            @ApiResponse(code = 404, message = "Ticket not found")
    })
    @PostMapping("/{bookingId}")
    ResponseEntity<TicketDto> generatePaidTicket(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            )
            @PathVariable Long bookingId);

    @ApiOperation(value = "Edit Ticket by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ticket has been updated"),
            @ApiResponse(code = 404, message = "Ticket not found")
    })
    @PatchMapping("/{id}")
    ResponseEntity<?> updateTicket(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            ) @PathVariable Long id,
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDto ticketDto);

    @ApiOperation(value = "Delete Ticket by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ticket has been removed"),
            @ApiResponse(code = 404, message = "Ticket not found")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteTicketById(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            )
            @PathVariable Long id);
}