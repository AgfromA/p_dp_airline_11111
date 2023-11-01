package app.controllers.api;

import app.dto.search.SearchResult;
import app.enums.Airport;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(tags = "Search")
@Tag(name = "Search", description = "API поиска рейсов по заданными параметрам")
public interface SearchControllerApi {

    @RequestMapping(value = "/api/search", method = RequestMethod.GET)
    @ApiOperation(value = "Create new search",
            notes = "Минимально необходимые поля для корректной работы контроллера:\n" +
                    " \"from\": {\"airportCode\": \"value\"},\n" +
                    " \"to\": {\"airportCode\": \"value\"},\n" +
                    " \"departureDate\": \"yyyy-mm-dd\",\n" +
                    " \"numberOfPassengers\": value (value - mast be bigger then 0)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "returned SearchResult"),
            @ApiResponse(code = 400, message = "search return error. Check validField "),
            @ApiResponse(code = 404, message = "Destinations not found")
    })
    ResponseEntity<SearchResult> search(

            @ApiParam(name = "airportFrom", value = "airportFrom")
            @RequestParam(value = "airportFrom") Airport airportFrom,

            @ApiParam(name = "airportTo", value = "airportTo")
            @RequestParam(value = "airportTo") Airport airportTo,

            @ApiParam(name = "departureDate", value = "departureDate")
            @RequestParam(value = "departureDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,

            @ApiParam(name = "returnDate", value = "returnDate")
            @RequestParam(value = "returnDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,

            @ApiParam(name = "numberOfPassengers", value = "numberOfPassengers")
            @RequestParam(value = "numberOfPassengers") Integer numberOfPassengers);
}



