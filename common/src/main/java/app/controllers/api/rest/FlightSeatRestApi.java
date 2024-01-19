package app.controllers.api.rest;

import app.dto.FlightSeatDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "FlightSeat REST")
@Tag(name = "FlightSeat REST", description = "API для операций с посадочными местами на рейс")
public interface FlightSeatRestApi {

    @RequestMapping(value = "/api/flight-seats", method = RequestMethod.GET)
    @ApiOperation(value = "Get all FlightSeats with optional pagination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    ResponseEntity<List<FlightSeatDto>> getAllFlightSeats(@PageableDefault()
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/flight-seats/filtered", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of FlightSeats by code of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeats found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    ResponseEntity<List<FlightSeatDto>> getAllFlightSeatsFiltered(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size,
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id"
            )
            @RequestParam(required = false) Long flightId,
            @ApiParam(
                    name = "isSold",
                    value = "FlightSeat.isSold"
            )
            @RequestParam(required = false) Boolean isSold,
            @ApiParam(
                    name = "isRegistered",
                    value = "FlightSeat.isRegistered"
            )
            @RequestParam(required = false) Boolean isRegistered);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    ResponseEntity<FlightSeatDto> getFlightSeat(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id
    );

    @RequestMapping(value = "/api/flight-seats", method = RequestMethod.POST)
    @ApiOperation(value = "Create new Flight Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "FlightSeat created"),
            @ApiResponse(code = 400, message = "FlightSeat not created")
    })
    ResponseEntity<FlightSeatDto> createFlightSeat(
            @ApiParam(
                    name = "flightSeat",
                    value = "FlightSeat"
            )
            @RequestBody
            @Valid
            FlightSeatDto flightSeat);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Update FlightSeat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat edited"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<FlightSeatDto> updateFlightSeat(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable(value = "id") Long id,
            @ApiParam(
                    name = "FlightSeat",
                    value = "FlightSeat",
                    required = true
            )
            @RequestBody
            @Valid
            FlightSeatDto flightSeat);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "FlightSeat deleted"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    ResponseEntity<String> deleteFlightSeat(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id"
            ) @PathVariable(value = "id") Long id
    );

    @RequestMapping(value = "/api/generate", method = RequestMethod.POST)
    @ApiOperation(value = "Generate FlightSeats for provided Flight based on Aircraft's Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeats existed"),
            @ApiResponse(code = 201, message = "FlightSeats generated"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Flight with this id not found")
    })
    ResponseEntity<List<FlightSeatDto>> generateFlightSeats(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @RequestParam
            Long flightId);
}