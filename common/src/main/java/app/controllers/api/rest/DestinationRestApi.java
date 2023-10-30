package app.controllers.api.rest;

import app.dto.DestinationDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Api(tags = "Destination REST")
@Tag(name = "Destination REST", description = "API для операций с пунктами назначения (прилет/вылет)")
public interface DestinationRestApi {

    @RequestMapping(value = "/api/destinations", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all Destinations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Destinations found"),
            @ApiResponse(code = 404, message = "Destinations not found")
    })
    ResponseEntity<Page<DestinationDTO>> getAllPagesDestinationsDTO(@PageableDefault(sort = {"id"})
                                                @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(10) Integer size,

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
    @RequestMapping(value = "/api/destinations", method = RequestMethod.POST)
    ResponseEntity<DestinationDTO> createDestinationDTO(
            @ApiParam(
                    name = "Destination",
                    value = "Destination"
            )
            @RequestBody DestinationDTO destinationDTO);

    @ApiOperation(value = "Edit Destination by id")
    @ApiResponse(code = 200, message = "Destination has been updated")
    @RequestMapping(value = "/api/destinations/{id}", method = RequestMethod.PATCH)

    ResponseEntity<DestinationDTO> updateDestinationDTOById(
            @ApiParam(
                    name = "id",
                    value = "Destination.id"
            ) @PathVariable("id") Long id,
            @ApiParam(
                    name = "Destination",
                    value = "Destination"
            )

            @RequestBody DestinationDTO destinationDTO);

    @ApiOperation("Delete Destination by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Destination deleted"),
    })
    @RequestMapping(value = "/api/destinations/{id}", method = RequestMethod.DELETE)
    ResponseEntity<HttpStatus> deleteDestinationById(
            @ApiParam(
                    name = "id",
                    value = "Destination.id",
                    required = true
            )
            @PathVariable("id") Long id);

}