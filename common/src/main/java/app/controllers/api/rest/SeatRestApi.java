package app.controllers.api.rest;

import app.dto.SeatDto;
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
import java.util.List;

@Api(tags = "Seat REST")
@Tag(name = "Seat REST", description = "API для операций с физическими местами в самолете")
public interface SeatRestApi {

    @RequestMapping(value = "/api/seats", method = RequestMethod.GET)
    @ApiOperation(value = "Get all Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats found"),
            @ApiResponse(code = 404, message = "Seats not found")
    })
    ResponseEntity<List<SeatDto>> getAllSeats(@PageableDefault()
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "size", required = false) Integer size,
                                              @ApiParam(
                                                      name = "aircraftId",
                                                      value = "Aircraft.id"
                                              )
                                              @RequestParam(value = "aircraftId", required = false) Long aircraftId);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat found"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    ResponseEntity<SeatDto> getSeat(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/seats", method = RequestMethod.POST)
    @ApiOperation(value = "Create new Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "seat created"),
            @ApiResponse(code = 400, message = "seat not created")
    })
    ResponseEntity<SeatDto> createSeat(
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDto seatDto);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Edit Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat edited"),
            @ApiResponse(code = 400, message = "seat failed to edit"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    ResponseEntity<SeatDto> updateSeat(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDto seatDto);

    @RequestMapping(value = "/api/seats/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat deleted"),
            @ApiResponse(code = 404, message = "seat not found"),
    })
    ResponseEntity<String> deleteSeat(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id);

    @RequestMapping(value = "/api/seats/aircraft/{aircraftId}", method = RequestMethod.POST)
    @ApiOperation(value = "Generate Seats for provided Aircraft based on Aircraft's model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats existed"),
            @ApiResponse(code = 201, message = "Seats generated"),
            @ApiResponse(code = 400, message = "Seats not created"),
            @ApiResponse(code = 404, message = "Aircraft with this id not found")
    })
    ResponseEntity<List<SeatDto>> generateSeats(@PathVariable("aircraftId") Long aircraftId);
}