package app.controllers.api.rest;

import app.dto.PassengerDTO;
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

import javax.validation.Valid;

@Api(tags = "Passenger REST")
@Tag(name = "Passenger REST", description = "API для операций с пассажирами")
@RequestMapping("/api/passengers")
public interface PassengerRestApi {

    @ApiOperation(value = "Get list of all Passengers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passengers found"),
            @ApiResponse(code = 404, message = "Passengers not found")
    })
    @GetMapping
    ResponseEntity<Page<PassengerDTO>> getAllPagesPassengersDTO(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    );

    @ApiOperation(value = "Get list of all Passengers filtered")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger found"),
            @ApiResponse(code = 400, message = "Passenger not found")
    })
    @GetMapping("/filter")
    ResponseEntity<Page<PassengerDTO>> getAllPagesPassengersDTOFiltered(
            @PageableDefault(sort = {"id"}) Pageable pageable,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "serialNumberPassport", required = false) String serialNumberPassport
    );

    @ApiOperation(value = "Get Passenger by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger found"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<PassengerDTO> getPassengerDTOById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable Long id);

    @ApiOperation(value = "Create new Passenger", notes = "Create method requires in model field \"@type\": \"Passenger\"")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Passenger created"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping
    ResponseEntity<PassengerDTO> createPassengerDTO(
            @ApiParam(
                    name = "Passenger",
                    value = "Passenger model",
                    required = true
            )
            @RequestBody @Valid PassengerDTO passengerDTO);

    @ApiOperation(value = "Edit Passenger", notes = "Update method requires in model field \"@type\": \"Passenger\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger updated"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @PatchMapping(value = "/{id}")
    ResponseEntity<PassengerDTO> updatePassengerDTOById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "Passenger",
                    value = "Passenger model"
            )
            @RequestBody
            @Valid PassengerDTO passengerDTO);

    @ApiOperation(value = "Delete Passenger by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger deleted"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deletePassengerById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable Long id);
}