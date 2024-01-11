package app.controllers.api.rest;

import app.dto.TicketDTO;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
    ResponseEntity<List<TicketDTO>> getAllPagesTicketsDTO(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @ApiOperation(value = "Get Ticket by ticketNumber")
    @ApiResponse(code = 200, message = "Found the ticket")
    @GetMapping("/{ticketNumber}")
    ResponseEntity<TicketDTO> getTicketDTOByTicketNumber(
            @ApiParam(
                    name = "ticketNumber",
                    value = "ticketNumber",
                    example = "SD-2222"
            )
            @PathVariable("ticketNumber") String ticketNumber);

    @ApiOperation(value = "Create new Ticket")
    @ApiResponse(code = 201, message = "Ticket created")
    @PostMapping
    ResponseEntity<TicketDTO> createTicketDTO(
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDTO ticketDTO);

    @ApiOperation(value = "Edit Ticket by \"id\"")
    @ApiResponse(code = 200, message = "Ticket has been updated")
    @PatchMapping("/{id}")
    ResponseEntity<?> updateTicketById(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            ) @PathVariable Long id,
            @ApiParam(
                    name = "ticket",
                    value = "Ticket model"
            )
            @RequestBody @Valid TicketDTO ticketDTO);

    @ApiOperation(value = "Delete Ticket by \"id\"")
    @ApiResponse(code = 200, message = "Ticket has been removed")
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteTicketById(
            @ApiParam(
                    name = "id",
                    value = "Ticket.id"
            )
            @PathVariable Long id);
}