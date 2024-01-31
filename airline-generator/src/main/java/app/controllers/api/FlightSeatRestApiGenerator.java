package app.controllers.api;

import app.dto.FlightSeatDto;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "FlightSeat REST GENERATOR")
@Tag(name = "FlightSeat REST GENERATOR", description = "API для генерации flightSeat")
public interface FlightSeatRestApiGenerator {
    @RequestMapping(value = "/api/generate/flight-seat", method = RequestMethod.POST)
    @ApiOperation(value = "Generate Flight Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Flight Seat generated"),
            @ApiResponse(code = 400, message = "Flight Seat not generated")
    })
    ResponseEntity<Page<FlightSeatDto>> generateFlightSeatDTO(
            @ApiParam(
                    value = "Amount of flightSeats",
                    example = "10"
            )
            @RequestParam(name = "amt") @Min(1) Integer amt);
}
