package app.controllers.api.rest;

import app.dto.FlightDto;
import app.enums.FlightStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Flight REST")
@Tag(name = "Flight REST", description = "API для операций с рейсами")
public interface FlightRestApi {

    @RequestMapping(value = "/api/flights", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Flights")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flights found"),
            @ApiResponse(code = 204, message = "Flights not found")
    })
    ResponseEntity<Page<FlightDto>> getAllFlights(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Flight by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight found"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDto> getFlight(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/flights", method = RequestMethod.POST)
    @ApiOperation(value = "Create Flight")
    @ApiResponse(code = 201, message = "Flight created")
    ResponseEntity<FlightDto> createFlight(
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDto flight);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight updated"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDto> updateFlight(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDto flight);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Flight by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Flight deleted"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<HttpStatus> deleteFlight(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/flights/status", method = RequestMethod.GET)
    @ApiOperation(value = "Get all flight statuses")
    @ApiResponse(code = 200, message = "Flight statuses found")
    ResponseEntity<FlightStatus[]> getAllFlightStatus();
}