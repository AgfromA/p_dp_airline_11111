package app.controllers.api.rest;

import app.dto.DestinationDTO;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Destination REST")
@Tag(name = "Destination REST", description = "API для операций с пунктами назначения (прилет/вылет)")
@RequestMapping("/api/destinations")
public interface DestinationRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Destinations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Destinations found"),
            @ApiResponse(code = 404, message = "Destinations not found")
    })
    ResponseEntity<Page<DestinationDTO>> getAllPagesDestinationsDTO(@PageableDefault(sort = {"id"}) Pageable pageable,

                                                                    @ApiParam(
                    name = "cityName",
                    value = "cityName",
                    example = "Волгоград"
            )
            @RequestParam(value = "cityName", required = false) String cityName,
                                                                    @ApiParam(
                    name = "countryName",
                    value = "countryName"
            )
            @RequestParam(value = "countryName", required = false) String countryName,
                                                                    @ApiParam(
                    name = "timezone",
                    value = "timezone",
                    example = "gmt%20%2b5"
            )
            @RequestParam(value = "timezone", required = false) String timezone);

    @ApiOperation(value = "Create new Destination")
    @ApiResponse(code = 201, message = "Destination created")
    @PostMapping
    ResponseEntity<DestinationDTO> createDestinationDTO(
            @ApiParam(
                    name = "Destination",
                    value = "Destination"
            )
            @RequestBody DestinationDTO destinationDTO);

    @ApiOperation(value = "Edit Destination by id")
    @ApiResponse(code = 200, message = "Destination has been updated")
    @PatchMapping("/{id}")
    ResponseEntity<DestinationDTO> updateDestinationDTOById(
            @ApiParam(
                    name = "id",
                    value = "Destination.id"
            ) @PathVariable Long id,
            @ApiParam(
                    name = "Destination",
                    value = "Destination"
            )

            @RequestBody DestinationDTO destinationDTO);

    @ApiOperation("Delete Destination by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Destination deleted"),
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteDestinationById(
            @ApiParam(
                    name = "id",
                    value = "Destination.id",
                    required = true
            )
            @PathVariable Long id);

}