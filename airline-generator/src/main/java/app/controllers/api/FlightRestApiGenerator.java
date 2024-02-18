package app.controllers.api;

import app.dto.FlightDto;
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

@Api(tags = "Flight API Generator")
@Tag(name = "Flight API Generator", description = "API для генерации рейсов")
public interface FlightRestApiGenerator {

    @RequestMapping(value = "/api/generate/flight", method = RequestMethod.POST)
    @Operation(summary = "Генерация рейсов")
    ResponseEntity<Page<FlightDto>> generateFlightDTO(@Parameter(description = "Количество сущностей", example = "10") @RequestParam("") @Min(1) Integer amt);
}