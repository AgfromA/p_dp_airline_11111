package app.controllers.api.rest;


import app.dto.TimezoneDTO;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Api(tags = "Timezone REST")
@Tag(name = "Timezone REST", description = "API для операций с временными зонами")
@RequestMapping("/api/timezones")
public interface TimezoneRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Timezone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezones found"),
            @ApiResponse(code = 404, message = "Timezones not found")
    })
    ResponseEntity<Page<TimezoneDTO>> getAllPagesTimezonesDTO(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size
    );

    @ApiOperation(value = "Get Timezone by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Timezone found"),
            @ApiResponse(code = 404, message = "Timezone not found")
    })
    @GetMapping("/{id}")
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
    @PostMapping
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
    @PatchMapping("/{id}")
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
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteTimezoneById(
            @ApiParam(
                    name = "id",
                    value = "Timezone.id",
                    required = true
            )
            @PathVariable Long id);
}