package app.controllers.api.rest;

import app.dto.TimezoneDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Timezone API")
@Tag(name = "Timezone API", description = "API для операций с часовыми поясами. Сущность не связана с логикой остального приложения")
public interface TimezoneRestApi {

    @RequestMapping(value = "/api/timezones", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<TimezoneDto>> getAllTimezones(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size
    );

    @Operation(summary = "Получение сущности")
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.GET)
    ResponseEntity<TimezoneDto> getTimezoneById(@Parameter(description = "ID сущности", required = true) @PathVariable Long id);

    @Operation(summary = "Создание сущности")
    @RequestMapping(value = "/api/timezones", method = RequestMethod.POST)
    ResponseEntity<TimezoneDto> createTimezone(@Parameter(description = "Часовой пояс") @RequestBody @Valid TimezoneDto timezoneDto);

    @Operation(summary = "Изменение сущности")
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.PATCH)
    ResponseEntity<TimezoneDto> updateTimezoneById(
            @Parameter(description = "ID сущности", required = true) @PathVariable Long id,
            @Parameter(description = "Часовой пояс") @RequestBody @Valid TimezoneDto timezoneDto);

    @Operation(summary = "Удаление сущности")
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deleteTimezoneById(@Parameter(description = "ID сущности", required = true) @PathVariable Long id);
}