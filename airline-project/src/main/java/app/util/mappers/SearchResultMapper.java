package app.util.mappers;

import app.dto.SearchResultDTO;
import app.entities.Flight;
import app.entities.search.SearchResult;
import app.services.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SearchResultMapper {
    private final SearchService searchService;

    private final FlightMapper flightMapper;

    public SearchResult convertToSearchResultEntity(SearchResultDTO searchResultDTO) {
        var searchResult = new SearchResult();
        searchResult.setId(searchResultDTO.getId());
        searchResult.setSearch(searchService.getSearchById(searchResultDTO.getSearchId()));
        List<Flight> departFlights = searchResultDTO.getDepartFlight().stream().map(f -> flightMapper.convertToFlightEntity(f)).collect(Collectors.toList());
        searchResult.setDepartFlight(departFlights);
        if (searchResultDTO.getReturnFlight() != null) {
            List<Flight> returnFlights = searchResultDTO.getReturnFlight().stream().map(f -> flightMapper.convertToFlightEntity(f)).collect(Collectors.toList());
            searchResult.setReturnFlight(returnFlights);
        }
        return searchResult;
    }


}
