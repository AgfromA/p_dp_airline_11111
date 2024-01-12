package app.controllers.api.rest;

import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.enums.CategoryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Api(tags = "FlightSeat REST")
@Tag(name = "FlightSeat REST", description = "API для операций с посадочными местами на рейс")
public interface FlightSeatRestApi {

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    ResponseEntity<FlightSeatDTO> getFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id
    );

    @RequestMapping(value = "/api/flight-seats/all-flight-seats", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of FlightSeat by code of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seats found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    ResponseEntity<List<FlightSeatDTO>> getAllFlightSeats(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id"
            )
            @RequestParam(required = false) Optional<Long> flightId,
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

    @RequestMapping(value = "/api/flight-seats/cheapest", method = RequestMethod.GET)
    @ApiOperation(value = "Get cheapest FlightSeat by flightId and seat category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 204, message = "FlightSeat not found"),
            @ApiResponse(code = 404, message = "Provided Flight not found")
    })
    ResponseEntity<List<FlightSeatDTO>> getCheapestByFlightIdAndSeatCategory(
            @RequestParam(name = "flightID") Long flightID,
            @RequestParam(name = "category") CategoryType category
    );

    @RequestMapping(value = "/api/flight-seats/seats/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get free seats on Flight by it's \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "free seats found"),
            @ApiResponse(code = 204, message = "no data found")
    })
    ResponseEntity<List<FlightSeatDTO>> getFreeSeatsById(
            @PageableDefault(sort = {"id"}) Pageable pageable,
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable Long id);

    @RequestMapping(value = "/api/flight-seats/all-flight-seats/{flightId}", method = RequestMethod.POST)
    @ApiOperation(value = "Generate FlightSeats for provided Flight based on Aircraft's Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeats existed"),
            @ApiResponse(code = 201, message = "FlightSeats generated"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Flight with this id not found")
    })
    ResponseEntity<Set<FlightSeatDTO>> generateAllFlightSeatsByFlightId(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable
            Long flightId);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Update FlightSeat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat edited"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    ResponseEntity<FlightSeatDTO> updateFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id,
            @ApiParam(
                    name = "flightSeat",
                    value = "FlightSeat DTO",
                    required = true
            )
            @RequestBody
            @Valid FlightSeatDTO flightSeatDTO);

    @RequestMapping(value = "/api/flight-seats/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete FlightSeat by id")
    //@DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "FlightSeat deleted"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    ResponseEntity<String> deleteFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id"
            ) @PathVariable Long id
    );

    @RequestMapping(value = "/api/flight-seats/seats", method = RequestMethod.POST)
    @ApiOperation(value = "Create new Flight Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Flight Seat created"),
            @ApiResponse(code = 400, message = "Flight Seat not created")
    })
    ResponseEntity<FlightSeatDTO> createFlightSeat(
            @ApiParam(
                    name = "flightSeat",
                    value = "FlightSeat DTO"
            )
            @RequestBody @Valid FlightSeatDTO flightSeatDTO);

    @RequestMapping(value = "/api/flight-seats/seats/all", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Seats DTO")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seat found"),
            @ApiResponse(code = 404, message = "Seat not found")
    })
    ResponseEntity<List<SeatDTO>> getAllSeat();
}