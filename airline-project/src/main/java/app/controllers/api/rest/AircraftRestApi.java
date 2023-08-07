package app.controllers.api.rest;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(tags = "Aircraft REST")
@Tag(name = "Aircraft REST", description = "API для операций с самолётом")
@RequestMapping("/api/aircrafts")
public interface AircraftRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Aircrafts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aircrafts found"),
            @ApiResponse(code = 204, message = "Aircrafts not found")})
    ResponseEntity<Page<AircraftDTO>> getAllPagesAircraftsDTO(Pageable pageable);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Aircraft by it's \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aircraft found"),
            @ApiResponse(code = 404, message = "Aircraft not found")})
    ResponseEntity<AircraftDTO> getAircraftDTOById(
            @ApiParam(name = "id", value = "Aircraft.id")
            @PathVariable("id") Long id);

    @PostMapping
    @ApiOperation(value = "Create new Aircraft")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Aircraft created")})
    ResponseEntity<Aircraft> createAircraft(
            @ApiParam(
                    name = "Aircraft",
                    value = "Aircraft model"
            )
            @RequestBody @Valid AircraftDTO aircraftDTO);

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update Aircraft by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aircraft updated"),
            @ApiResponse(code = 404, message = "Aircraft not found")})
    ResponseEntity<Aircraft> updateAircraftById(
            @ApiParam(
                    name = "id",
                    value = "Aircraft.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "Aircraft",
                    value = "Aircraft model"
            )
            @RequestBody @Valid AircraftDTO aircraftDTO);

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Aircraft by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aircraft deleted"),
            @ApiResponse(code = 404, message = "Aircraft not found")})
    ResponseEntity<HttpStatus> deleteAircraftById(
            @ApiParam(
                    name = "id",
                    value = "Aircraft.id"
            )
            @PathVariable("id") Long id);
}