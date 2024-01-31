package app.controllers.api;

import app.dto.FlightDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "Flight REST GENERATOR")
@Tag(name = "Flight REST GENERATOR", description = "API для генерации рейсов")
public interface FlightRestApiGenerator {
    @RequestMapping(value = "/api/generate/flight", method = RequestMethod.POST)
    @ApiOperation(value = "Generate Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Flight generated"),
            @ApiResponse(code = 400, message = "Flight not generated")
    })
    ResponseEntity<Page<FlightDto>> generateFlightDTO(
            @ApiParam(
                    value = "Amount of flights",
                    example = "10"
            )
            @RequestParam(name = "amt") @Min(1) Integer amt);
}
