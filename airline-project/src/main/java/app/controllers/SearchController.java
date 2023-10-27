package app.controllers;

import app.controllers.api.SearchControllerApi;
import app.dto.search.SearchResult;
import app.enums.Airport;
import app.services.interfaces.SearchService;
import app.util.LogsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController implements SearchControllerApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<SearchResult> get(
            Airport from,
            Airport to,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer numberOfPassengers) {

        log.debug("incoming Airport from = {}", LogsUtils.objectToJson(from));
        log.debug("incoming Airport to = {}", LogsUtils.objectToJson(to));
        if (from == null || to == null) {
            log.info("Destination.from is null or Destination.to is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("incoming numberOfPassengers = {}", LogsUtils.objectToJson(numberOfPassengers));
        if (numberOfPassengers == null || numberOfPassengers < 1) {
            log.info("NumberOfPassengers is incorrect");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("incoming departureDate = {}", LogsUtils.objectToJson(departureDate));
        log.debug("incoming returnDate = {}", LogsUtils.objectToJson(returnDate));
        if (returnDate != null && !(returnDate.isAfter(departureDate))) {
            log.info("DepartureDate must be earlier then ReturnDate");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SearchResult searchResult = searchService.getSearch(from, to, departureDate, returnDate, numberOfPassengers);
        if (searchResult.getDepartFlights().isEmpty() && searchResult.getReturnFlights().isEmpty()) {
            log.info("Destination not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Search result found = {}", searchResult);
        log.debug("Outgoing data = {}", LogsUtils.objectToJson(searchResult));
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
}




