package app.controllers.api.rest;

import app.dto.PassengerDto;
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

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Passenger REST")
@Tag(name = "Passenger REST", description = "API для операций с пассажирами")
public interface PassengerRestApi {

    @ApiOperation(value = "Get list of all Passengers filtered")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger found"),
            @ApiResponse(code = 400, message = "Passenger not found")
    })
    @RequestMapping(value = "/api/passengers", method = RequestMethod.GET)
    ResponseEntity<List<PassengerDto>> getAllPassengers(
            @PageableDefault(sort = {"id"})
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
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
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.GET)
    ResponseEntity<PassengerDto> getById(
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
    @RequestMapping(value = "/api/passengers", method = RequestMethod.POST)
    ResponseEntity<PassengerDto> create(
            @ApiParam(
                    name = "Passenger",
                    value = "Passenger model",
                    required = true
            )
            @RequestBody @Valid PassengerDto passengerDTO);

    @ApiOperation(value = "Edit Passenger", notes = "Update method requires in model field \"@type\": \"Passenger\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger updated"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.PATCH)
    ResponseEntity<PassengerDto> updateById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable(value = "id") Long id,
            @ApiParam(
                    name = "Passenger",
                    value = "Passenger model"
            )
            @RequestBody
            @Valid PassengerDto passengerDTO);

    @ApiOperation(value = "Delete Passenger by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Passenger deleted"),
            @ApiResponse(code = 404, message = "Passenger not found")
    })
    @RequestMapping(value = "/api/passengers/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deleteById(
            @ApiParam(
                    name = "id",
                    value = "User.id",
                    required = true
            )
            @PathVariable Long id);
}