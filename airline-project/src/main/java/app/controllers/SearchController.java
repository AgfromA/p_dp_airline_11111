package app.controllers;

import app.controllers.api.SearchControllerApi;
import app.entities.search.Search;
import app.entities.search.SearchResult;
import app.enums.Airport;
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
    public ResponseEntity<SearchResult> get(Search search) {
        log.debug("findSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        if (search.getFrom() == null || search.getTo() == null) {
            log.info("findSearch: Destination.from is null or Destination.to is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (search.getNumberOfPassengers() == null || search.getNumberOfPassengers() < 1) {
                log.info("findSearch: NumberOfPassengers not found");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                if (search.getReturnDate() != null && !search.getReturnDate().isAfter(search.getDepartureDate())) {
                    log.info("findSearch: DepartureDate must be earlier then ReturnDate");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                var searchResult = searchService.getSearch(search);
                if (searchResult.getDepartFlight().isEmpty() && searchResult.getReturnFlight().isEmpty()) {
                    log.info("findSearch: Destination not found");
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                log.info("findSearch: search result found = {}", searchResult);
                log.debug("findSearch: outgoing data = {}", LogsUtils.objectToJson(searchResult));
                return new ResponseEntity<>(searchResult, HttpStatus.OK);
            }
        }
    }
}



