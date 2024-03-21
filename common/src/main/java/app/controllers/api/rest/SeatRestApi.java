package app.controllers.api.rest;

import app.dto.SeatDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Seat API")
@Tag(name = "Seat API", description = "API для операций с физическими местами в самолете")
public interface SeatRestApi {

    @RequestMapping(value = "/api/seats", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<SeatDto>> getAllSeats(@Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
                                              @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size,
                                              @Parameter(description = "ID самолета") @RequestParam(value = "aircraftId", required = false) Long aircraftId);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.GET)
    @Operation(summary = "Получение сущности")
    ResponseEntity<SeatDto> getSeat(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/seats", method = RequestMethod.POST)
    @Operation(summary = "Создание сущности")
    ResponseEntity<SeatDto> createSeat(@Parameter(description = "Место в самолете") @RequestBody @Valid SeatDto seatDto);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.PATCH)
    @Operation(summary = "Изменение сущности")
    ResponseEntity<SeatDto> updateSeat(
            @Parameter(description = "ID сущности") @PathVariable Long id,
            @Parameter(description = "Место в самолете") @RequestBody @Valid SeatDto seatDto);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление сущности")
    ResponseEntity<String> deleteSeat(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/seats/generate", method = RequestMethod.POST)
    @Operation(summary = "Генерация сидений для указанного самолета", description = "Если у самолета уже есть хотя бы одно сидение, то генерация не сработает. Генерируются в зависимости от модели самолета")
    ResponseEntity<Page<SeatDto>> generateSeats(@Parameter(description = "ID самолета") @RequestParam("aircraftId") Long aircraftId);
}