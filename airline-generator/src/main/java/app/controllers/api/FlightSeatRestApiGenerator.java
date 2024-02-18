package app.controllers.api;

import app.dto.FlightSeatDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;

@Api(tags = "FlightSeat API Generator")
@Tag(name = "FlightSeat API Generator", description = "API для генерации посадочных мест")
public interface FlightSeatRestApiGenerator {

    @RequestMapping(value = "/api/generate/flight-seat", method = RequestMethod.POST)
    @Operation(summary = "Генерация посадочных мест")
    ResponseEntity<Page<FlightSeatDto>> generateFlightSeatDTO(@Parameter(description = "Количество сущностей", example = "10") @RequestParam("") @Min(1) Integer amt);
}