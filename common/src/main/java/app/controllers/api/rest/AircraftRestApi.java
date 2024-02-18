package app.controllers.api.rest;

import app.dto.AircraftDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = "Aircraft API")
@Tag(name = "Aircraft API", description = "API для операций с самолётом")
public interface AircraftRestApi {

    @GetMapping("/api/aircrafts")
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<AircraftDto>> getAllAircrafts(
            @Parameter(name = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(name = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size);

    @GetMapping("/api/aircrafts/{id}")
    @Operation(summary = "Получение сущности")
    ResponseEntity<AircraftDto> getAircraft(@Parameter(description = "ID сущности") @PathVariable @Min(0) Long id);

    @PostMapping("/api/aircrafts")
    @Operation(summary = "Создание сущности")
    ResponseEntity<AircraftDto> createAircraft(@Parameter(description = "Самолет") @RequestBody @Valid AircraftDto aircraftDto);

    @PatchMapping("/api/aircrafts/{id}")
    @Operation(summary = "Изменение сущности")
    ResponseEntity<AircraftDto> updateAircraft(
            @Parameter(description = "ID сущности") @PathVariable @Min(0) Long id,
            @Parameter(description = "Самолет") @RequestBody @Valid AircraftDto aircraftDto);

    @DeleteMapping("/api/aircrafts/{id}")
    @Operation(summary = "Удаление сущности")
    ResponseEntity<HttpStatus> deleteAircraft(@Parameter(description = "ID сущности") @PathVariable @Min(0) Long id);
}