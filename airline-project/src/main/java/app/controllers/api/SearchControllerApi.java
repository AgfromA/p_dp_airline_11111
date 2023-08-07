package app.controllers.api;

import app.dto.SearchResultDTO;
import app.entities.search.Search;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(tags = "Search")
@Tag(name = "Search", description = "API поиска рейсов по заданными параметрам")
@RequestMapping("/api/search")
public interface SearchControllerApi {

    @PostMapping
    @ApiOperation(value = "Create new search",
            notes = "Минимально необходимые поля для корректной работы контроллера:\n" +
                    " \"from\": {\"airportCode\": \"value\"},\n" +
                    " \"to\": {\"airportCode\": \"value\"},\n" +
                    " \"departureDate\": \"yyyy-mm-dd\",\n" +
                    " \"numberOfPassengers\": value (value - mast be bigger then 0)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "returned \"id\" of SearchResult"),
            @ApiResponse(code = 400, message = "search return error. check validField "),
            @ApiResponse(code = 404, message = "Destinations not found")
    })
    ResponseEntity<SearchResultDTO> saveSearch(
            @ApiParam(
                    name = "search",
                    value = "Search model"
            )
            @RequestBody @Valid Search search);

    @GetMapping("/{id}")
    @ApiOperation(value = "Get search result by \"id\"")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "search result found"),
            @ApiResponse(code = 404, message = "search result not found")
    })
    ResponseEntity<SearchResultDTO> getSearchResultDTOById(

            @ApiParam(
                    name = "id",
                    value = "SearchResult.id"
            )
            @PathVariable("id") Long id);
}