package app.controllers.api.rest;

import app.dto.DestinationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Destination API")
@Tag(name = "Destination API", description = "API для операций с пунктами назначения рейсов")
public interface DestinationRestApi {

    @RequestMapping(value = "/api/destinations", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации. Можно указать уточняющие параметры.")
    ResponseEntity<Page<DestinationDto>> getAllDestinations(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "Город", example = "Волгоград") @RequestParam(value = "cityName", required = false) String cityName,
            @Parameter(description = "Страна") @RequestParam(value = "countryName", required = false) String countryName,
            @Parameter(description = "Часовой пояс", example = "gmt%20%2b5") @RequestParam(value = "timezone", required = false) String timezone);

    @Operation(summary = "Создание сущности")
    @RequestMapping(value = "/api/destinations", method = RequestMethod.POST)
    ResponseEntity<DestinationDto> createDestination(@Parameter(description = "Пункт назначения", required = true) @RequestBody DestinationDto timezoneDto);

    @Operation(summary = "Изменение сущности")
    @RequestMapping(value = "/api/destinations/{id}", method = RequestMethod.PATCH)
    ResponseEntity<DestinationDto> updateDestination(
            @Parameter(description = "ID сущности") @PathVariable Long id,
            @Parameter(description = "Пункт назначения") @RequestBody DestinationDto timezoneDto);

    @ApiOperation("Удаление сущности")
    @RequestMapping(value = "/api/destinations/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deleteDestination(@Parameter(description = "ID сущности", required = true) @PathVariable Long id);
}