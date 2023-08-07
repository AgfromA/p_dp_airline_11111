package app.controllers.api.rest;

import app.dto.TicketDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Api(tags = "Ticket REST")
@Tag(name = "Ticket REST", description = "API для операций с билетами")
@RequestMapping("/api/tickets")
public interface TicketRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Tickets")
    ResponseEntity<Page<TicketDTO>> getAllPagesTicketsDTO(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Max(10) Integer size);

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