package app.controllers.api.rest;

import app.dto.TicketDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;

@Api(tags = "Ticket API")
@Tag(name = "Ticket API", description = "API для операций с билетами. Создается после бронирования (Booking)")
@RequestMapping("/api/tickets")
public interface TicketRestApi {

    @GetMapping
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<TicketDto>> getAllTickets(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size);

    @Operation(summary = "Получение билета по его номеру")
    @GetMapping("/{ticketNumber}")
    ResponseEntity<TicketDto> getTicketByNumber(@Parameter(description = "Номер билета", example = "SD-2222") @PathVariable("ticketNumber") String ticketNumber);


    @Operation(summary = "Получение PDF-файла билета по номеру билета")
    @GetMapping(value = "/pdf/{ticketNumber}", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<InputStreamResource> getPdfByTicketNumber
            (@Parameter(description = "Номер билета", example = "CC-3000")
             @PathVariable("ticketNumber") String ticketNumber) throws FileNotFoundException;

    @Operation(summary = "Создание сущности", description = "Если ticketNumber не был передан в запросе, то он будет сгенерирован бекендом. ticketNumber должен быть уникальным. Билет будет создан только в случае, если связанное бронирование оплачено. Для одного бронирования может быть создан только один билет. Поля билета должны быть такие же, как у связанного бронирования")
    @PostMapping
    ResponseEntity<TicketDto> createTicket(@Parameter(description = "Билет") @RequestBody @Valid TicketDto ticketDto);

    @Operation(summary = "Создание билета для указанного бронирования", description = "Билет будет создан только в случае, если бронирование оплачено")
    @PostMapping("/{bookingId}")
    ResponseEntity<TicketDto> generatePaidTicket(@Parameter(description = "ID бронирования") @PathVariable("bookingId") Long bookingId);

    @Operation(summary = "Изменение сущности", description = "ID бронирования не может быть изменено")
    @PatchMapping("/{id}")
    ResponseEntity<?> updateTicket(
            @Parameter(description = "ID сущности") @PathVariable Long id,
            @Parameter(description = "Билет") @RequestBody @Valid TicketDto ticketDto);

    @Operation(summary = "Удаление сущности")
    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteTicketById(@Parameter(description = "ID сущности") @PathVariable Long id);
}