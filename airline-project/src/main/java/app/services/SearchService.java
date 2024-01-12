package app.services;

import app.entities.Flight;
import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.enums.Airport;
import app.mappers.FlightMapper;
import app.utils.aop.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final FlightService flightService;
    private final DestinationService destinationService;
    private final FlightSeatService flightSeatService;

    @Transactional
    @Loggable
    public SearchResult search(Airport from, Airport to, LocalDate departureDate,
                               LocalDate returnDate, Integer numberOfPassengers) {

        var search = new Search(from, to, departureDate, returnDate, numberOfPassengers);

        var searchResult = new SearchResult();
        searchResult.setSearch(search);

        List<Flight> searchDepartFlight = new ArrayList<>();
        List<Flight> searchReturnFlight = new ArrayList<>();

        addDirectDepartFlightsToSearchDepartFlight(search, searchDepartFlight);
        addNonDirectDepartFlightsToSearchDepartFlight(search, searchDepartFlight);

        var searchDepartFlightDto = searchDepartFlight.stream()
                .map(departFlight -> Mappers.getMapper(FlightMapper.class)
                        .toDto(departFlight, flightService))
                .collect(Collectors.toList());

        searchResult.setDepartFlights(searchDepartFlightDto);

        if (search.getReturnDate() == null) {
            searchResult.setReturnFlights(new ArrayList<>());
        } else {
            addDirectReturnFlightsToSearchReturnFlight(search, searchReturnFlight);
            addNonDirectReturnFlightsToSearchReturnFlight(search, searchReturnFlight);


            var searchReturnFlightDto = searchReturnFlight.stream()
                    .map(returnFlight -> Mappers.getMapper(FlightMapper.class)
                            .toDto(returnFlight, flightService))
                    .collect(Collectors.toList());

            searchResult.setReturnFlights(searchReturnFlightDto);
        }
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
    private void addNonDirectReturnFlightsToSearchReturnFlight(Search search, List<Flight> searchFlightList) {
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
                destinationService.getDestinationByAirportCode(search.getTo()).getId().intValue(),
                destinationService.getDestinationByAirportCode(search.getFrom()).getId().intValue(),
                Date.valueOf(search.getReturnDate())
        );
    }

    @Loggable
    private boolean checkFlightForNumberSeats(Flight f, Search search) {
        return (flightSeatService.getNumberOfFreeSeatOnFlight(f) - search.getNumberOfPassengers()) >= 0;
    }
}