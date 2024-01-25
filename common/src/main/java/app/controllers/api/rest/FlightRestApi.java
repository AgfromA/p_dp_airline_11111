package app.controllers.api.rest;

import app.dto.FlightDto;
import app.enums.FlightStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "Flight REST")
@Tag(name = "Flight REST", description = "API для операций с рейсами")
public interface FlightRestApi {

    @RequestMapping(value = "/api/flights/param", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Flights by params")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flights found"),
            @ApiResponse(code = 204, message = "Flights not found")
    })
    ResponseEntity<List<FlightDto>> getAllFlightsByDestinationsAndDates(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size,

            @ApiParam(value = "Departure cityName", example = "Москва")
            @RequestParam(name = "cityFrom", required = false) String cityFrom,

            @ApiParam(value = "Arrival cityName", example = "Омск")
            @RequestParam(name = "cityTo", required = false) String cityTo,

            @ApiParam(value = "Departure Data-Time", example = "2022-12-10T15:56:49")
            @RequestParam(name = "dateStart", required = false) String dateStart,

            @ApiParam(value = "Arrival Data-Time", example = "2022-12-10T15:57:49")
            @RequestParam(name = "dateFinish", required = false) String dateFinish);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Flight by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight found"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDto> getFlightById(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id);


    @RequestMapping(value = "/api/flights/status", method = RequestMethod.GET)
    @ApiOperation(value = "Get all flight statuses")
    @ApiResponse(code = 200, message = "Flight statuses found")
    ResponseEntity<FlightStatus[]> getAllFlightStatus();

    @RequestMapping(value = "/api/flights", method = RequestMethod.POST)
    @ApiOperation(value = "Create Flight")
    @ApiResponse(code = 201, message = "Flight created")
    ResponseEntity<FlightDto> createFlight(
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDto flightDto);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight updated"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDto> updateFlightById(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDto flightDto);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Flight by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Flight deleted"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<HttpStatus> deleteFlightById(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/flights", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Flights")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flights found"),
            @ApiResponse(code = 204, message = "Flights not found")
    })
    ResponseEntity<List<FlightDto>> getAllFlightsDTO(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

}