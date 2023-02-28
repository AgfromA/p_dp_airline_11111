package app.controllers.rest;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.services.FlightSeatServiceImpl;
import app.util.mappers.FlightSeatMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "FlightSeat REST")
@Tag(name = "FlightSeat REST", description = "Операции с посадочными местами на рейс")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight-seats")
public class FlightSeatRestController {

    private final FlightSeatServiceImpl flightSeatService;
    private final FlightSeatMapper flightSeatMapper;


    @ApiOperation(value = "Get FlightSeat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seat found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightSeatDTO> getFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id",
                    required = true
            )
            @PathVariable Long id
    ) {
        log.info("getFlightSeatById: get FlightSeat by id={}", id);
        return (flightSeatService.findById(id) == null) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(new FlightSeatDTO(flightSeatService.findById(id)));
    }

    @ApiOperation(value = "Get list of FlightSeat by code of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seats found"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/all-flight-seats/{flightId}")
    public ResponseEntity<Set<FlightSeatDTO>> getFlightSeatsByFlightId(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable Long flightId,
            @ApiParam(
                    name = "isSold",
                    value = "FlightSeat.isSold"
            )
            @RequestParam(required = false) Boolean isSold) {

        if (isSold != null && !isSold) {
            log.info("getFlightSeatById: get not sold FlightSeat by id={}", flightId);
            Set<FlightSeat> result = flightSeatService.findNotSoldById(flightId);
            return (result.isEmpty()) ?
                    ResponseEntity.notFound().build() :
                    ResponseEntity.ok(result
                            .stream()
                            .map(FlightSeatDTO::new)
                            .collect(Collectors.toSet()));
        }

        log.info("getFlightSeatsByFlightId: get flight seats by flightId. flightId={}", flightId);
        Set<FlightSeat> result = flightSeatService.findByFlightId(flightId);
        return (result.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(result
                        .stream()
                        .map(FlightSeatDTO::new)
                        .collect(Collectors.toSet())
                );
    }

    @ApiOperation(value = "Add FlightSeat by ID of Flight")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seats added"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/all-flight-seats/{flightId}")
    public ResponseEntity<Set<FlightSeatDTO>> addFlightSeatsByFlightID(
            @ApiParam(
                    name = "flightId",
                    value = "Flight.id",
                    required = true
            )
            @PathVariable
            Long flightId) {
        log.info("addFlightSeatsByFlightNumber: add flight seats by flightId. flightId={}", flightId);
        return new ResponseEntity<>(flightSeatService.addFlightSeatsByFlightId(flightId)
                .stream()
                .map(FlightSeatDTO::new)
                .collect(Collectors.toSet()),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit FlightSeat by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "flight seat edited"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<FlightSeatDTO> editFlightSeatById(
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
            @Valid FlightSeatDTO flightSeatDTO) {
        log.info("editFlightSeatById: edit flight seat by id. id={}", id);

        if (flightSeatService.findById(id) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new FlightSeatDTO(flightSeatService.editFlightSeat(id, flightSeatMapper.convertToFlightSeatEntity(flightSeatDTO))));
    }

    @ApiOperation(value = "Delete FlightSeat by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlightSeatById(
            @ApiParam(
                    name = "id",
                    value = "FlightSeat.id"
            ) @PathVariable Long id
    ) {
        try {
            flightSeatService.deleteById(id);
            log.info("deleteFlightSeatById: FlightSeat with id={} deleted", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteFlightSeatById: error while deleting - FlightSeat with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}