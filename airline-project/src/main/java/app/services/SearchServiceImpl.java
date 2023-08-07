package app.services;

import app.entities.Flight;
import app.entities.search.Search;
import app.entities.search.SearchResult;
import app.repositories.SearchRepository;
import app.repositories.SearchResultProjection;
import app.repositories.SearchResultRepository;
import app.services.interfaces.DestinationService;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.SearchService;
import app.util.LogsUtils;
import app.util.aop.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;
    private final FlightService flightService;
    private final DestinationService destinationService;
    private final FlightSeatService flightSeatService;
    private final SearchResultRepository searchResultRepository;


    @Override
    @Transactional
    @Loggable
    public SearchResult saveSearch(Search search) {
        log.debug("saveSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        search.setFrom(destinationService.getDestinationByAirportCode(search.getFrom().getAirportCode()));
        search.setTo(destinationService.getDestinationByAirportCode(search.getTo().getAirportCode()));
        searchRepository.save(search);
        var searchResult = searchDirectAndNonDirectFlights(search);
        log.debug("saveSearch: output data, searchResult = {}", LogsUtils.objectToJson(searchResult));
        return searchResult;
    }

    @Override
    @Loggable
    public Search getSearchById(long id) {
        log.debug("findSearchById: incoming data, search \"id\" = {}", id);
        var search = searchRepository.findById(id).orElse(null);
        log.debug("findSearchById: output data, search = {}", LogsUtils.objectToJson(search));
        return search;
    }

    @Loggable
    private SearchResult searchDirectAndNonDirectFlights(Search search) {
        log.debug("searchDirectAndNonDirectFlights: incoming data, search = {}", LogsUtils.objectToJson(search));
        var searchResult = new SearchResult();
        searchResult.setSearch(search);

        List<Flight> searchDepartFlight = new ArrayList<>();
        List<Flight> searchReturnFlight = new ArrayList<>();

        addDirectDepartFlightsToSearchDepartFlight(search, searchDepartFlight);
        addNonDirectDepartFlightsToSearchDepartFlight(search, searchDepartFlight);
        searchResult.setDepartFlight(searchDepartFlight);

        if (search.getReturnDate() == null) {
            searchResult.setReturnFlight(null);
        } else {
            addDirectReturnFlightsToSearchReturnFlight(search, searchReturnFlight);
            addNonDirectDepartFlightsToSearchReturnFlight(search, searchReturnFlight);
            searchResult.setReturnFlight(searchReturnFlight);
        }
        saveSearchResult(searchResult);
        log.debug("searchDirectAndNonDirectFlights: output data, searchResult \"id\" = {}", searchResult.getId());
        return searchResult;
    }

    @Loggable
    private void addDirectDepartFlightsToSearchDepartFlight(Search search, List<Flight> searchFlightList) {
        var departFlight = getDirectDepartFlights(search);
        //проверка рейсов на наличие мест. если места есть, то рейс добавлется в список рейсов
        for (Flight f : departFlight) {
            if (checkFlightForNumberSeats(f, search)) {
                searchFlightList.add(f);
            }
        }
    }

    @Loggable
    private void addDirectReturnFlightsToSearchReturnFlight(Search search, List<Flight> searchFlightList) {
        var returnFlight = getDirectReturnFlights(search);
        //проверка прямых рейсов на наличие мест. если места есть, то рейс добавлется в список рейсов
        for (Flight f : returnFlight) {
            if (checkFlightForNumberSeats(f, search)) {
                searchFlightList.add(f);
            }
        }
    }

    @Loggable
    private void addNonDirectDepartFlightsToSearchDepartFlight(Search search, List<Flight> searchFlightList) {
        var nonDirectDepartFlights = getNonDirectDepartFlights(search);
        //проверка непрямых рейсов на наличие мест. если места есть, то соответствующая пара добавляется в список рейсов
        for (Flight f : nonDirectDepartFlights) {
            if (checkFlightForNumberSeats(f, search)) {
                for (Flight connected_flight : nonDirectDepartFlights) {
                    if (f.getTo().equals(connected_flight.getFrom()) && checkFlightForNumberSeats(connected_flight, search)) {
                        searchFlightList.add(f);
                        searchFlightList.add(connected_flight);
                    }
                }
            }
        }
    }

    @Loggable
    private void addNonDirectDepartFlightsToSearchReturnFlight(Search search, List<Flight> searchFlightList) {
        var nonDirectReturnFlights = getNonDirectReturnFlights(search);
        //проверка непрямых обратных рейсов на наличие мест: если места есть, то соответствующая пара добавляется в список рейсов
        for (Flight f : nonDirectReturnFlights) {
            if (checkFlightForNumberSeats(f, search)) {
                for (Flight connected_flight : nonDirectReturnFlights) {
                    if (f.getTo().equals(connected_flight.getFrom()) && checkFlightForNumberSeats(connected_flight, search)) {
                        searchFlightList.add(f);
                        searchFlightList.add(connected_flight);
                    }
                }
            }
        }
    }

    @Loggable
    private List<Flight> getDirectDepartFlights(Search search) {
        return flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                search.getFrom().getAirportCode(),
                search.getTo().getAirportCode(),
                Date.valueOf(search.getDepartureDate())
        );
    }

    @Loggable
    private List<Flight> getDirectReturnFlights(Search search) {
        return flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                search.getTo().getAirportCode(),
                search.getFrom().getAirportCode(),
                Date.valueOf(search.getReturnDate())
        );
    }

    @Loggable
    private List<Flight> getNonDirectDepartFlights(Search search) {
        return flightService.getListNonDirectFlightsByFromAndToAndDepartureDate(
                search.getFrom().getId().intValue(),
                search.getTo().getId().intValue(),
                Date.valueOf(search.getDepartureDate())
        );
    }

    @Loggable
    private List<Flight> getNonDirectReturnFlights(Search search) {
        return flightService.getListNonDirectFlightsByFromAndToAndDepartureDate(
                search.getTo().getId().intValue(),
                search.getFrom().getId().intValue(),
                Date.valueOf(search.getReturnDate())
        );
    }

    @Loggable
    private boolean checkFlightForNumberSeats(Flight f, Search search) {
        return (flightSeatService.getNumberOfFreeSeatOnFlight(f) - search.getNumberOfPassengers()) >= 0;
    }

    @Override
    @Loggable
    public void saveSearchResult(SearchResult searchResult) {
        log.debug("saveSearchResult: incoming data, searchResult = {}", LogsUtils.objectToJson(searchResult));
        searchResultRepository.save(searchResult);
        log.debug("saveSearchResult: output data is void");
    }

    @Override
    @Loggable
    public SearchResultProjection getSearchResultProjectionByID(Long id) {
        log.debug("findSearchResultByID: incoming data, searchResult \"id\" = {}", id);
        var searchResult = searchResultRepository.findAllProjectedBy(id);
        log.debug("findSearchResultByID: output data, searchResult = {}", LogsUtils.objectToJson(searchResult));
        return searchResult;
    }
}
