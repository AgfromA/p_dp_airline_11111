package app.controllers.api.rest;

import app.dto.FlightDto;
import app.enums.FlightStatus;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Flight API")
@Tag(name = "Flight API", description = "API для операций с рейсами")
public interface FlightRestApi {

    @RequestMapping(value = "/api/flights", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<FlightDto>> getAllFlights(
            @Parameter(name = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.GET)
    @Operation(summary = "Получение сущности")
    ResponseEntity<FlightDto> getFlight(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/flights", method = RequestMethod.POST)
    @Operation(summary = "Создание сущности")
    ResponseEntity<FlightDto> createFlight(@Parameter(description = "Рейс") @RequestBody FlightDto flight);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.PATCH)
    @Operation(summary = "Изменение сущности")
    ResponseEntity<FlightDto> updateFlight(
            @Parameter(description = "ID сущности") @PathVariable Long id,
            @Parameter(description = "Рейс") @RequestBody FlightDto flight);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление сущности")
    ResponseEntity<HttpStatus> deleteFlight(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/flights/status", method = RequestMethod.GET)
    @Operation(summary = "Получение всех возможных статусов рейса")
    ResponseEntity<FlightStatus[]> getAllFlightStatus();
}