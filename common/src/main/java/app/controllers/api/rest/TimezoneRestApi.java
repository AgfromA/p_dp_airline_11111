package app.controllers.api.rest;


import app.dto.TimezoneDTO;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "Timezone REST")
@Tag(name = "Timezone REST", description = "API для операций с временными зонами")
public interface TimezoneRestApi {

    @RequestMapping(value = "/api/timezones", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all Timezone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezones found"),
            @ApiResponse(code = 404, message = "Timezones not found")
    })
    ResponseEntity<List<TimezoneDTO>> getAllPagesTimezonesDTO(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    );

    @ApiOperation(value = "Get Timezone by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezone found"),
            @ApiResponse(code = 404, message = "Timezone not found")
    })
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.GET)
    ResponseEntity<TimezoneDTO> getTimezoneDTOById(
            @ApiParam(
                    name = "id",
                    value = "Timezone.id",
                    required = true
            )
            @PathVariable("id") Long id);

    @ApiOperation(value = "Create new Timezone")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Timezone created"),
            @ApiResponse(code = 405, message = "Bad request")
    })
    @RequestMapping(value = "/api/timezones", method = RequestMethod.POST)
    ResponseEntity<TimezoneDTO> createTimezoneDTO(
            @ApiParam(
                    name = "timezone",
                    value = "Timezone model"
            )
            @RequestBody @Valid TimezoneDTO timezoneDTO);

    @ApiOperation(value = "Edit Timezone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezone has been updated"),
            @ApiResponse(code = 404, message = "Timezone not found")
    })
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.PATCH)
    ResponseEntity<TimezoneDTO> updateTimezoneDTOById(
            @ApiParam(
                    name = "id",
                    value = "Timezone.id",
                    required = true
            ) @PathVariable("id") Long id,
            @ApiParam(
                    name = "Timezone",
                    value = "Timezone model"
            )
            @RequestBody @Valid TimezoneDTO timezoneDTO);

    @ApiOperation(value = "Delete Timezone by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezone has been removed"),
            @ApiResponse(code = 404, message = "Timezone not found")
    })
    @RequestMapping(value = "/api/timezones/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deleteTimezoneById(
            @ApiParam(
                    name = "id",
                    value = "Timezone.id",
                    required = true
            )
            @PathVariable("id") Long id);
}