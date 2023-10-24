package app.controllers;

import app.controllers.api.rest.SearchControllerApi;
import app.entities.account.search.Search;
import app.entities.account.search.SearchResult;
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
    public ResponseEntity<SearchResult> save(Search search) {
        log.debug("saveSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        if (search.getFrom() == null || search.getTo() == null) {
            log.info("saveSearch: Destinations cannot be null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (search.getReturnDate() != null && !search.getReturnDate().isAfter(search.getDepartureDate())) {
                log.info("saveSearch: DepartureDate must be earlier then ReturnDate");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            var searchResult = searchService.saveSearch(search);
            if (searchResult.getDepartFlights().isEmpty() && searchResult.getReturnFlights().isEmpty()) {
                log.info("saveSearch: Flights not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            log.info("saveSearch: new search result saved = {}", searchResult);
            log.debug("saveSearch: outgoing data = {}", LogsUtils.objectToJson(searchResult));
            return new ResponseEntity<>(searchResult, HttpStatus.CREATED);
        }
    }
}