package app.controllers.api.rest;

import app.dto.FlightSeatDTO;
import app.enums.CategoryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Api(tags = "FlightSeat REST")
@Tag(name = "FlightSeat REST", description = "API для операций с посадочными местами на рейс")
@RequestMapping("/api/flight-seats")
public interface FlightSeatRestApi {

    @ApiOperation(value = "Get FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 404, message = "FlightSeat not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<FlightSeatDTO> getFlightSeatDTOById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id
    );

    @ApiOperation(value = "Get list of FlightSeat by code of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seats found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/all-flight-seats")
    ResponseEntity<Page<FlightSeatDTO>> getAllPagesFlightSeatsDTO(
            @PageableDefault(sort = {"id"}) Pageable pageable,
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

    @ApiOperation(value = "Get cheapest FlightSeat by flightId and seat category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat found"),
            @ApiResponse(code = 204, message = "FlightSeat not found"),
            @ApiResponse(code = 404, message = "Provided Flight not found")
    })
    @GetMapping("/cheapest")
    ResponseEntity<List<FlightSeatDTO>> getCheapestByFlightIdAndSeatCategory(
            @RequestParam(name = "flightID") Long flightID,
            @RequestParam(name = "category") CategoryType category
    );

    @GetMapping("/seats/{id}")
    @ApiOperation(value = "Get free seats on Flight by it's \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "free seats found"),
            @ApiResponse(code = 204, message = "no data found")
    })
    ResponseEntity<Page<FlightSeatDTO>> getPagesFreeSeatsById(
            @PageableDefault(sort = {"id"}) Pageable pageable,
            @ApiParam(
                    name = "id",
                    value = "Flight.id"
            )
            @PathVariable Long id);

    @ApiOperation(value = "Generate FlightSeats for provided Flight based on Aircraft's Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeats existed"),
            @ApiResponse(code = 201, message = "FlightSeats generated"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Flight with this id not found")
    })
    @PostMapping("/all-flight-seats/{flightId}")
    ResponseEntity<Set<FlightSeatDTO>> generateAllFlightSeatsDTOByFlightId(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable
            Long flightId);

    @ApiOperation(value = "Update FlightSeat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FlightSeat edited"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PatchMapping("/{id}")
    ResponseEntity<FlightSeatDTO> updateFlightSeatDTOById(
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

    @ApiOperation(value = "Delete FlightSeat by id")
    @DeleteMapping("/{id}")
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
}
