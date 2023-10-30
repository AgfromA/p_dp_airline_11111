package app.controllers.api.rest;

import app.dto.FlightDTO;
import app.enums.FlightStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Flight REST")
@Tag(name = "Flight REST", description = "API для операций с рейсами")
public interface FlightRestApi {

    @RequestMapping(value = "/api/flights/all", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Flights or Flights by params")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flights found"),
            @ApiResponse(code = 204, message = "Flights not found")
    })
    ResponseEntity<Page<FlightDTO>> getAllPagesFlightsByDestinationsAndDates(
            @ApiParam(value = "Departure cityName", example = "Москва")
            @RequestParam(name = "cityFrom", required = false) String cityFrom,
            @ApiParam(value = "Arrival cityName", example = "Омск")
            @RequestParam(name = "cityTo", required = false) String cityTo,
            @ApiParam(value = "Departure Data-Time", example = "2022-12-10T15:56:49")
            @RequestParam(name = "dateStart", required = false) String dateStart,
            @ApiParam(value = "Arrival Data-Time", example = "2022-12-10T15:57:49")
            @RequestParam(name = "dateFinish", required = false) String dateFinish,
            @PageableDefault(sort = {"id"}) Pageable pageable);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Flight by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight found"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDTO> getFlightDTOById(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/flights/filter/dates", method = RequestMethod.GET)
    @ApiOperation(value = "Get Flight by \"id\" and dates given as params")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight found"),
            @ApiResponse(code = 404, message = "flight not found")
    })
    ResponseEntity<FlightDTO> getFlightDTOByIdAndDates(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @RequestParam(name = "id") Long id,
            @ApiParam(
                    value = "Departure Data-Time",
                    example = "2022-12-10T15:56:49"
            )
            @RequestParam(name = "date_start") String start,
            @ApiParam(
                    value = "Arrival Data-Time",
                    example = "2022-12-10T15:57:49"
            )
            @RequestParam(name = "date_finish") String finish);

    @RequestMapping(value = "/api/flights/status", method = RequestMethod.GET)
    @ApiOperation(value = "Get all flight statuses")
    @ApiResponse(code = 200, message = "Flight statuses found")
    ResponseEntity<FlightStatus[]> getAllFlightStatus();

    @RequestMapping(value = "/api/flights", method = RequestMethod.POST)
    @ApiOperation(value = "Create Flight")
    @ApiResponse(code = 201, message = "Flight created")
    ResponseEntity<FlightDTO> createFlight(
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDTO flightDTO);

    @RequestMapping(value = "/api/flights/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight updated"),
            @ApiResponse(code = 404, message = "Flight not found")
    })
    ResponseEntity<FlightDTO> updateFlightById(
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "flight",
                    value = "Flight model"
            )
            @RequestBody FlightDTO flightDTO);

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
}