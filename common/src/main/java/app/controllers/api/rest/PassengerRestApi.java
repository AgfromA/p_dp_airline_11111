package app.controllers.api.rest;

import app.dto.PassengerDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Passenger API")
@Tag(name = "Passenger API", description = "API для операций с пассажирами")
public interface PassengerRestApi {

    @Operation(summary = "Получение всех сущностей с пагинацией/без пагинации")
    @RequestMapping(value = "/api/passengers", method = RequestMethod.GET)
    ResponseEntity<Page<PassengerDto>> getAllPassengers(
            @Parameter(description = "Номер страницы") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Количество элементов на странице") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "Имя") @RequestParam(value = "firstName", required = false) String firstName,
            @Parameter(description = "Фамилия") @RequestParam(value = "lastName", required = false) String lastName,
            @Parameter(description = "Email") @RequestParam(value = "email", required = false) String email,
            @Parameter(description = "Номер паспорта") @RequestParam(value = "serialNumberPassport", required = false) String serialNumberPassport
    );

    @Operation(summary = "Получение сущности")
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.GET)
    ResponseEntity<PassengerDto> getPassenger(@Parameter(description = "ID сущности") @PathVariable Long id);

    @Operation(summary = "Создание сущности")
    @RequestMapping(value = "/api/passengers", method = RequestMethod.POST)
    ResponseEntity<PassengerDto> createPassenger(@Parameter(description = "Пассажир", required = true) @RequestBody @Valid PassengerDto passengerDTO);

    @Operation(summary = "Изменение сущности")
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.PATCH)
    ResponseEntity<PassengerDto> updatePassenger(
            @Parameter(description = "ID сущности", required = true) @PathVariable Long id,
            @Parameter(description = "Пассажир") @RequestBody @Valid PassengerDto passengerDTO);

    @Operation(summary = "Удаление сущности")
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deletePassenger(@Parameter(description = "ID сущности", required = true) @PathVariable Long id);
}