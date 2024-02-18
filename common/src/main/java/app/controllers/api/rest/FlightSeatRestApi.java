package app.controllers.api.rest;

import app.dto.FlightSeatDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "FlightSeat API")
@Tag(name = "FlightSeat API", description = "API для операций с посадочными местами на рейс. Привязаны к физическим местам (Seat) в самолете (Aircraft)")
public interface FlightSeatRestApi {

    @RequestMapping(value = "/api/flight-seats", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<FlightSeatDto>> getAllFlightSeats(@PageableDefault()
                                                          @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
                                                          @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size,
                                                          @Parameter(description = "ID рейса") @RequestParam(value = "flightId", required = false) Long flightId,
                                                          @Parameter(description = "Только непроданные места") @RequestParam(value = "isSold", required = false) Boolean isSold,
                                                          @Parameter(description = "Только незарегистрированные места") @RequestParam(value = "isRegistered", required = false) Boolean isRegistered);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.GET)
    @Operation(summary = "Получение сущности")
    ResponseEntity<FlightSeatDto> getFlightSeat(@Parameter(description = "ID сущности", required = true) @PathVariable Long id);

    @RequestMapping(value = "/api/flight-seats", method = RequestMethod.POST)
    @Operation(summary = "Создание сущности")
    ResponseEntity<FlightSeatDto> createFlightSeat(@Parameter(description = "Посадочное место") @RequestBody @Valid FlightSeatDto flightSeat);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.PATCH)
    @Operation(summary = "Изменение сущности")
    ResponseEntity<FlightSeatDto> updateFlightSeat(
            @Parameter(description = "ID сущности", required = true) @PathVariable Long id,
            @Parameter(description = "Посадочное место", required = true) @RequestBody @Valid FlightSeatDto flightSeat);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление сущности")
    ResponseEntity<String> deleteFlightSeat(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/flight-seats/generate", method = RequestMethod.POST)
    @Operation(
            summary = "Генерация посадочных мест для указанного рейса (только при наличии Seat у Aircraft)",
            description = "Если у рейса уже есть хотя бы одно посадочное место, то генерация не сработает"
    )
    ResponseEntity<Page<FlightSeatDto>> generateFlightSeats(@Parameter(description = "ID рейса", required = true) @RequestParam("flightId") Long flightId);
}