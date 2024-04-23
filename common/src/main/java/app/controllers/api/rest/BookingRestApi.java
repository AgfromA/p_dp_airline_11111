package app.controllers.api.rest;

import app.dto.BookingDto;
import app.dto.BookingUpdateDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RequestMapping("/api/bookings")
@Api(tags = "Booking API")
@Tag(name = "Booking API", description = "API для операций с бронированием посадочных мест")
public interface BookingRestApi {

    @GetMapping
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<BookingDto>> getAllBookings(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size
    );

    @GetMapping("/{id}")
    @Operation(summary = "Получение сущности")
    ResponseEntity<BookingDto> getBooking(@Parameter(description = "ID сущности") @PathVariable @Min(1) Long id);

    @PostMapping
    @Operation(summary = "Создание сущности", description = "Если бронирование не было оплачено в течение 600 секунд, то оно автоматически становится просроченным. Первоначальный статус бронирования NOT_PAID - генерируется бекендом. Бронирование может быть создано только для незабронированных и непроданных сидений")
    ResponseEntity<BookingDto> createBooking(@Parameter(description = "Бронирование") @RequestBody @Valid BookingDto bookingDto);

    @PatchMapping("/{id}")
    @Operation(summary = "Изменение сущности")
    ResponseEntity<BookingDto> updateBooking(
            @Parameter(description = "ID сущности") @PathVariable @Min(1) Long id,
            @Parameter(description = "Бронирование") @RequestBody @Valid BookingUpdateDto bookingDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление сущности")
    ResponseEntity<HttpStatus> deleteBooking(@Parameter(description = "ID сущности") @PathVariable @Min(1) Long id);
}