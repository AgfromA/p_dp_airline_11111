package app.controllers;

import app.controllers.api.SearchControllerApi;
import app.dto.search.SearchResult;
import app.entities.Category;
import app.enums.Airport;
import app.enums.CategoryType;
import app.exceptions.SearchControllerException;
import app.services.SearchService;
import app.utils.LogsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SearchController implements SearchControllerApi {

    private final SearchService searchService;

    @Override
    public ResponseEntity<SearchResult> search(
            Airport from,
            Airport to,
            LocalDate departureDate,
            LocalDate returnDate,
            Integer numberOfPassengers,
            CategoryType categoryOfSeats) {
        String errorMessage;

        log.debug("incoming Airport from = {}", LogsUtils.objectToJson(from));
        log.debug("incoming Airport to = {}", LogsUtils.objectToJson(to));
        if (from == null || to == null) {
            errorMessage = "Destination.from is null or Destination.to is null";
            log.info(errorMessage);
            throw new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        log.debug("incoming numberOfPassengers = {}", LogsUtils.objectToJson(numberOfPassengers));
        if (numberOfPassengers == null || numberOfPassengers < 1) {
            errorMessage = "NumberOfPassengers is incorrect";
            log.info(errorMessage);
            throw new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        log.debug("incoming categoryOfSeats = {}", LogsUtils.objectToJson(categoryOfSeats));
        if (categoryOfSeats == null) {
            errorMessage = "categoryOfSeats is incorrect";
            log.info(errorMessage);
            throw new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        log.debug("incoming departureDate = {}", LogsUtils.objectToJson(departureDate));
        log.debug("incoming returnDate = {}", LogsUtils.objectToJson(returnDate));
        if (returnDate != null && !(returnDate.isAfter(departureDate))) {
            errorMessage = "DepartureDate must be earlier then ReturnDate";
            log.info(errorMessage);
            throw new SearchControllerException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        try {
            SearchResult searchResult = searchService.search(from, to, departureDate, returnDate, numberOfPassengers,
                    categoryOfSeats);
            if (searchResult.getFlights().isEmpty()) {
                log.info("Flights not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            log.info("Search result found = {}", searchResult);
            log.debug("Outgoing data = {}", LogsUtils.objectToJson(searchResult));
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Search result error = {}", e.getMessage());
            throw new SearchControllerException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}