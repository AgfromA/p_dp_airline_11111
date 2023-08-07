package app.controllers.api.rest;

import app.dto.SeatDTO;
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

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Seat REST")
@Tag(name = "Seat REST", description = "API для операций с физическими местами в самолете")
@RequestMapping("/api/seats")
public interface SeatRestApi {

    @GetMapping
    @ApiOperation(value = "Get all Seats")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats found"),
            @ApiResponse(code = 404, message = "Seats not found")
    })
    ResponseEntity<Page<SeatDTO>> getAllPagesSeatsDTO(@PageableDefault() Pageable pageable);

    @GetMapping("/aircraft/{aircraftId}")
    @ApiOperation(value = "Get Seats by \"aircraftId\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats found"),
            @ApiResponse(code = 404, message = "Seats not found")
    })
    ResponseEntity<Page<SeatDTO>> getAllPagesSeatsDTOByAircraftId(
            @PageableDefault(sort = {"id"}, value = 30) Pageable pageable,
            @ApiParam(
                    name = "aircraftId",
                    value = "Aircraft.id"
            )
            @PathVariable("aircraftId") Long aircraftId);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat found"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    ResponseEntity<SeatDTO> getSeatDTOById(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id);

    @PostMapping
    @ApiOperation(value = "Create new Seat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "seat created"),
            @ApiResponse(code = 400, message = "seat not created")
    })
    ResponseEntity<SeatDTO> createSeatDTO(
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDTO seatDTO);

    @PostMapping("/aircraft/{aircraftId}")
    @ApiOperation(value = "Generate Seats for provided Aircraft based on Aircraft's model")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Seats existed"),
            @ApiResponse(code = 201, message = "Seats generated"),
            @ApiResponse(code = 400, message = "Seats not created"),
            @ApiResponse(code = 404, message = "Aircraft with this id not found")
    })
    ResponseEntity<List<SeatDTO>> generateSeatsDTOByAircraftId(@PathVariable("aircraftId") Long aircraftId);

    @PatchMapping("/{id}")
    @ApiOperation(value = "Edit Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat edited"),
            @ApiResponse(code = 400, message = "seat failed to edit"),
            @ApiResponse(code = 404, message = "seat not found")
    })
    ResponseEntity<SeatDTO> updateSeatDTOById(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "seat",
                    value = "Seat model"
            )
            @RequestBody @Valid SeatDTO seatDTO);

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Seat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "seat deleted"),
            @ApiResponse(code = 404, message = "seat not found"),
            @ApiResponse(code = 405, message = "seat is locked")
    })
    ResponseEntity<String> deleteSeatById(
            @ApiParam(
                    name = "id",
                    value = "Seat.id"
            )
            @PathVariable("id") Long id);
}