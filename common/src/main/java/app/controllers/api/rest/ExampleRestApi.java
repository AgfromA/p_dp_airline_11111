package app.controllers.api.rest;

import app.dto.ExampleDto;
import io.swagger.annotations.Api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "Example API")
@Tag(name = "Example API", description = "API для примера работы с контроллерами. Сущность не связана с логикой остального приложения")
public interface ExampleRestApi {

    @RequestMapping(value = "/api/example", method = RequestMethod.GET)
    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    ResponseEntity<Page<ExampleDto>> getAllExamples(@Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
                                                    @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/example{id}", method = RequestMethod.GET)
    @Operation(summary = "Получение сущности")
    ResponseEntity<ExampleDto> get(@Parameter(description = "ID сущности") @PathVariable Long id);

    @RequestMapping(value = "/api/example", method = RequestMethod.POST)
    @Operation(summary = "Создание сущности")
    ResponseEntity<ExampleDto> create(@Parameter(description = "Пример") @Valid @RequestBody ExampleDto exampleDto);

    @RequestMapping(value = "/api/example{id}", method = RequestMethod.PATCH)
    @Operation(summary = "Изменение сущности")
    ResponseEntity<ExampleDto> update(@Parameter(description = "ID сущности") @PathVariable Long id,
                                      @Parameter(description = "Пример") @Valid @RequestBody ExampleDto exampleDto);

    @RequestMapping(value = "/api/example{id}", method = RequestMethod.DELETE)
    @Operation(summary = "Удаление сущности")
    ResponseEntity<Void> delete(@Parameter(description = "ID сущности") @PathVariable Long id);
}