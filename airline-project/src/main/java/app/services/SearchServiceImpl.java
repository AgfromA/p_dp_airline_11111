package app.services;

import app.entities.Flight;
import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.mappers.FlightMapper;
import app.services.interfaces.DestinationService;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.SearchService;
import app.util.LogsUtils;
import app.util.aop.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final FlightService flightService;
    private final DestinationService destinationService;
    private final FlightSeatService flightSeatService;

    @Override
    @Loggable
    public SearchResult getSearch(Search search) {
        log.debug("findSearch: incoming data, search = {}", LogsUtils.objectToJson(search));
        var searchResult = searchDirectAndNonDirectFlights(search);
        log.debug("findSearchResult: output data, searchResult = {}", LogsUtils.objectToJson(searchResult));
        return searchResult;
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

        var searchDepartFlightDTO = searchDepartFlight.stream()
                .map(departFlight -> Mappers.getMapper(FlightMapper.class)
                        .flightToFlightDTO(departFlight))
                .collect(Collectors.toList());

        searchResult.setDepartFlights(searchDepartFlightDTO);

        if (search.getReturnDate() == null) {
            searchResult.setReturnFlights(new ArrayList<>());
        } else {
            addDirectReturnFlightsToSearchReturnFlight(search, searchReturnFlight);
            addNonDirectDepartFlightsToSearchReturnFlight(search, searchReturnFlight);

            var searchReturnFlightDTO = searchReturnFlight.stream()
                    .map(returnFlight -> Mappers.getMapper(FlightMapper.class)
                            .flightToFlightDTO(returnFlight))
                    .collect(Collectors.toList());

            searchResult.setReturnFlights(searchReturnFlightDTO);
        }
        log.debug("searchDirectAndNonDirectFlights: output data, searchResult = {}", searchResult);
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
                search.getFrom(),
                search.getTo(),
                Date.valueOf(search.getDepartureDate())
        );
    }


    @Loggable
    private List<Flight> getDirectReturnFlights(Search search) {
        return flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                search.getTo(),
                search.getFrom(),
                Date.valueOf(search.getReturnDate())
        );
    }

    @Loggable
    private List<Flight> getNonDirectDepartFlights(Search search) {
        return flightService.getListNonDirectFlightsByFromAndToAndDepartureDate(
                destinationService.getDestinationByAirportCode(search.getFrom()).getId().intValue(),
                destinationService.getDestinationByAirportCode(search.getTo()).getId().intValue(),
                Date.valueOf(search.getDepartureDate())
        );
    }

    @Loggable
    private List<Flight> getNonDirectReturnFlights(Search search) {
        return flightService.getListNonDirectFlightsByFromAndToAndDepartureDate(
                destinationService.getDestinationByAirportCode(search.getFrom()).getId().intValue(),
                destinationService.getDestinationByAirportCode(search.getTo()).getId().intValue(),
                Date.valueOf(search.getReturnDate())
        );
    }

    @Loggable
    private boolean checkFlightForNumberSeats(Flight f, Search search) {
        return (flightSeatService.getNumberOfFreeSeatOnFlight(f) - search.getNumberOfPassengers()) >= 0;
    }
}


