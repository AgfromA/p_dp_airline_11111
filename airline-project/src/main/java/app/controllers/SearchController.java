package app.controllers;

import app.controllers.api.SearchControllerApi;
import app.dto.SearchResultDTO;
import app.entities.search.Search;
import app.entities.search.SearchResult;
import app.repositories.SearchResultProjection;
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
        log.debug("saveSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        if (search.getFrom() == null) {
            log.info("saveSearch: Destination.from is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (search.getReturnDate() != null && !search.getReturnDate().isAfter(search.getDepartureDate())) {
                log.info("saveSearch: DepartureDate must be earlier then ReturnDate");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            var searchResult = searchService.saveSearch(search);
            if (searchResult.getDepartFlight().isEmpty()) {
                log.info("saveSearch: Destinations not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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