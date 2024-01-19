package app.services;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.dto.search.SearchResultCard;
import app.dto.search.SearchResultCardData;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.enums.Airport;
import app.utils.aop.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


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

        List<SearchResultCard> directFlights = getDirectFlights(search);

        searchResult.setFlights(directFlights);

        searchResult.getFlights().addAll(getNonDirectFlights(search));

        return searchResult;

    }

    @Loggable
    private List<Flight> addNonDirectDepartFlightsToSearchDepartFlight(Search search) {
        List<Flight> searchFlightList = new ArrayList<>();
        var nonDirectDepartFlights = getNonDirectDepartFlights(search);
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
        return searchFlightList;
    }

    @Loggable
    private List<Flight> addNonDirectReturnFlightsToSearchReturnFlight(Search search) {
        List<Flight> searchFlightList = new ArrayList<>();
        var nonDirectReturnFlights = getNonDirectReturnFlights(search);
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
        return searchFlightList;
    }

    private List<SearchResultCard> getNonDirectFlights(Search search) {
        List<Flight> departFlights = addNonDirectDepartFlightsToSearchDepartFlight(search);
        List<SearchResultCard> nonDirectFlights = new ArrayList<>();

        for (Flight flightD : departFlights) {
            SearchResultCard searchResultCard = new SearchResultCard();
            SearchResultCardData searchResultCardData = builderForSearchResultCardData(flightD);
            searchResultCard.setDataTo(searchResultCardData);
            Integer totalPriceDepart = findLowestFare(search, flightD);
            searchResultCard.setTotalPrice(totalPriceDepart);

            if (search.getReturnDate() != null) {
                List<Flight> returnFlights = addNonDirectReturnFlightsToSearchReturnFlight(search);
                for (Flight flightR : returnFlights) {
                    SearchResultCardData searchResultCardDataBack = builderForSearchResultCardData(flightR);
                    searchResultCard.setDataBack(searchResultCardDataBack);
                    Integer totalPriceReturn = totalPriceDepart + findLowestFare(search, flightR);
                    searchResultCard.setTotalPrice(totalPriceReturn);
                    nonDirectFlights.add(searchResultCard);
                }
            } else {
                nonDirectFlights.add(searchResultCard);
                searchResultCard.setTotalPrice(totalPriceDepart);
            }
        }
        return nonDirectFlights;
    }

    @Loggable
    private SearchResultCardData builderForSearchResultCardData(Flight flight) {
        return SearchResultCardData.builder()
                .airportFrom(flight.getFrom().getAirportCode())
                .airportTo(flight.getTo().getAirportCode())
                .flightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                        .plus(Duration.between(flight.getArrivalDateTime(),
                                flight.getDepartureDateTime())))
                .departureDateTime(flight.getDepartureDateTime())
                .arrivalDateTime(flight.getArrivalDateTime())
                .build();
    }

    public Integer findLowestFare(Search search, Flight flight) {
        Set<FlightSeat> sortedFlightSeats = flightSeatService.getSetFlightSeatsByFlightId(flight.getId());
        return sortedFlightSeats.stream()
                .sorted(Comparator.comparingInt(FlightSeat::getFare))
                .limit(search.getNumberOfPassengers())
                .mapToInt(FlightSeat::getFare)
                .sum();
    }

    @Loggable
    private List<SearchResultCard> getDirectFlights(Search search) {
        List<SearchResultCard> searchResultCardList = new ArrayList<>();
        List<Flight> returnFlights = new ArrayList<>();

        List<Flight> departFlights = flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                search.getFrom(),
                search.getTo(),
                Date.valueOf(search.getDepartureDate()));

        if (search.getReturnDate() != null) {
            returnFlights = flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                    search.getTo(),
                    search.getFrom(),
                    Date.valueOf(search.getReturnDate()));
        }

        for (Flight departFlight : departFlights) {
            if (checkFlightForNumberSeats(departFlight, search)) {
                SearchResultCard searchResultCard = new SearchResultCard();
                SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                searchResultCard.setDataTo(searchResultCardData);
                Integer totalPriceDepart = findLowestFare(search, departFlight);
                searchResultCard.setTotalPrice(totalPriceDepart);

                if (search.getReturnDate() != null) {
                    for (Flight returnFlight : returnFlights) {
                        if (checkFlightForNumberSeats(returnFlight, search)) {
                            SearchResultCardData searchResultCardDataBack = builderForSearchResultCardData(returnFlight);
                            searchResultCard.setDataBack(searchResultCardDataBack);
                            Integer totalPriceReturn = totalPriceDepart + findLowestFare(search, returnFlight);
                            searchResultCard.setTotalPrice(totalPriceReturn);
                            searchResultCardList.add(searchResultCard);
                        }
                    }
                } else {
                    searchResultCard.setTotalPrice(totalPriceDepart);
                    searchResultCardList.add(searchResultCard);
                }
            }
        }
        return searchResultCardList;
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
