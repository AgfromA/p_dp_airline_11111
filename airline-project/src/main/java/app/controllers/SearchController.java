package app.controllers;

import app.controllers.api.SearchControllerApi;
import app.dto.SearchResultDTO;
import app.entities.search.Search;
import app.exceptions.SearchRequestException;
import app.services.interfaces.SearchService;
import app.util.LogsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController implements SearchControllerApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<SearchResultDTO> saveSearch(Search search) {
        String errorMessage;

        log.debug("saveSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        if (search.getFrom() == null) {
            errorMessage = "saveSearch: Destination.from is null";
            log.error(errorMessage);
            throw new SearchRequestException(errorMessage, HttpStatus.BAD_REQUEST);
        } else {
            if (search.getReturnDate() != null && !search.getReturnDate().isAfter(search.getDepartureDate())) {
                errorMessage = "saveSearch: DepartureDate must be earlier then ReturnDate";
                log.error(errorMessage);
                throw new SearchRequestException(errorMessage, HttpStatus.BAD_REQUEST);
            }
            var searchResult = searchService.saveSearch(search);
            if (searchResult.getDepartFlight().isEmpty()) {
                errorMessage = "saveSearch: Destinations not found";
                log.error(errorMessage);
                throw new SearchRequestException(errorMessage, HttpStatus.NO_CONTENT);

            }
            log.info("saveSearch: new search result saved with id= {}", searchResult.getId());
            var result = new SearchResultDTO(searchResult);
            log.debug("saveSearch: outgoing data, searchResultDTO = {}", LogsUtils.objectToJson(result));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<SearchResultDTO> getSearchResultDTOById(Long id) {

        var searchResult = searchService.getSearchResultProjectionByID(id);
        if (searchResult != null) {
            log.info("getSearchResultById: find search result with id = {}", id);
            return new ResponseEntity<>(new SearchResultDTO(searchResult), HttpStatus.OK);
        } else {
            log.info("getSearchResultById: not find search result with id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}