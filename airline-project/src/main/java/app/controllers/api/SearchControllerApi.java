package app.controllers.api;

import app.entities.search.Search;
import app.entities.search.SearchResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    ResponseEntity<SearchResult> get(
            @ApiParam(
                    name = "search",
                    value = "Search model"
            )
            @RequestBody @Valid Search search);
}



