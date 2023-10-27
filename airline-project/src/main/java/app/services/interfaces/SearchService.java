package app.services.interfaces;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.enums.Airport;

import java.time.LocalDate;

public interface SearchService {

    SearchResult getSearch(Airport from, Airport to, LocalDate departureDate,
                           LocalDate returnDate, Integer numberOfPassengers);
}