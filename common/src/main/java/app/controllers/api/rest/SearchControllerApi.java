package app.controllers.api.rest;

import app.entities.account.search.Search;
import app.entities.account.search.SearchResult;


import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "Search")
@Tag(name = "Search", description = "API поиска рейсов по заданными параметрам")
public interface SearchControllerApi {

    @RequestMapping(value = "/api/search", method = RequestMethod.POST)
    @ApiOperation(value = "Create new search",
            notes = "Минимально необходимые поля для корректной работы контроллера:\n" +
                    " \"from\": {\"airportCode\": \"value\"},\n" +
                    " \"to\": {\"airportCode\": \"value\"},\n" +
                    " \"departureDate\": \"yyyy-mm-dd\",\n" +
                    " \"numberOfPassengers\": value (value - mast be bigger then 0)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "returned \"id\" of SearchResult"),
            @ApiResponse(code = 400, message = "search return error. check validField "),
            @ApiResponse(code = 404, message = "flights not found")
    })
    ResponseEntity<SearchResult> save(
            @ApiParam(
                    name = "search",
                    value = "Search model"
            )
            @RequestBody @Valid Search search);
}