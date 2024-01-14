package app.services;

import app.dto.search.SearchResultCard;
import app.dto.search.SearchResultCardData;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        List<SearchResultCard> directFlights = getDirectFlights(search);

        searchResult.setFlights(directFlights);
        searchResult.getFlights().addAll(getNonDirectFlights(search));

        return searchResult;
    }


    @Loggable
    private List<Flight> addNonDirectDepartFlightsToSearchDepartFlight(Search search) {
        List<Flight> searchFlightList = new ArrayList<>();
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
        return searchFlightList;
    }

    @Loggable
    private List<Flight> addNonDirectReturnFlightsToSearchReturnFlight(Search search) {
        List<Flight> searchFlightList = new ArrayList<>();
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
        return searchFlightList;
    }
    @Loggable
    private List <SearchResultCard> getNonDirectFlights(Search search) {
        List<Flight> departFlights = addNonDirectDepartFlightsToSearchDepartFlight(search);
        List<Flight> returnFlights = addNonDirectReturnFlightsToSearchReturnFlight(search);
        List<SearchResultCard> nonDirectFlights = new ArrayList<>();
        SearchResultCard searchResultCard = new SearchResultCard();
        for (Flight flightD : departFlights) {
            if (search.getReturnDate() != null) {
                for (Flight flightR : returnFlights) {
                    SearchResultCardData searchResultCardData = new SearchResultCardData();
                    searchResultCardData.setAirportFrom(flightD.getFrom().getAirportCode());
                    searchResultCardData.setAirportTo(flightD.getTo().getAirportCode());
                    searchResultCardData.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                            .plus(Duration.between(flightD.getDepartureDateTime(),
                                    flightD.getArrivalDateTime())));
                    searchResultCardData.setDepartureDateTime(flightD.getDepartureDateTime());
                    searchResultCardData.setArrivalDateTime(flightD.getArrivalDateTime());
                    searchResultCard.setDataTo(searchResultCardData);

                    SearchResultCardData searchResultCardDataBack = new SearchResultCardData();
                    searchResultCardDataBack.setAirportFrom(flightR.getFrom().getAirportCode());
                    searchResultCardDataBack.setAirportTo(flightR.getTo().getAirportCode());
                    searchResultCardDataBack.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                            .plus(Duration.between(flightR.getDepartureDateTime(),
                                    flightR.getArrivalDateTime())));
                    searchResultCardDataBack.setDepartureDateTime(flightR.getDepartureDateTime());
                    searchResultCardDataBack.setArrivalDateTime(flightR.getArrivalDateTime());
                    searchResultCard.setDataBack(searchResultCardDataBack);

                    nonDirectFlights.add(searchResultCard);
                }
            } else {
                SearchResultCardData searchResultCardData = new SearchResultCardData();
                searchResultCardData.setAirportFrom(flightD.getFrom().getAirportCode());
                searchResultCardData.setAirportTo(flightD.getTo().getAirportCode());
                searchResultCardData.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                        .plus(Duration.between(flightD.getDepartureDateTime(),
                                flightD.getArrivalDateTime())));
                searchResultCardData.setDepartureDateTime(flightD.getDepartureDateTime());
                searchResultCardData.setArrivalDateTime(flightD.getArrivalDateTime());
                searchResultCard.setDataTo(searchResultCardData);

                nonDirectFlights.add(searchResultCard);
            }
        }
        return nonDirectFlights;
    }

    @Loggable
    private List<SearchResultCard> getDirectFlights(Search search) {
        List<SearchResultCard> searchResultCardList = new ArrayList<>();
        List<Flight> returnFlights = new ArrayList<>();
        SearchResultCard searchResultCard = new SearchResultCard();
        List<Flight> flights = flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                search.getFrom(),
                search.getTo(),
                Date.valueOf(search.getDepartureDate()));
        if (search.getReturnDate() != null) {
            returnFlights = flightService.getListDirectFlightsByFromAndToAndDepartureDate(
                    search.getTo(),
                    search.getFrom(),
                    Date.valueOf(search.getReturnDate()));
        }
        for (Flight flight : flights) {
            if (checkFlightForNumberSeats(flight, search)) {
                if (search.getReturnDate() != null) {
                    for (Flight returnFlight : returnFlights) {
                        if (checkFlightForNumberSeats(flight, search)) {
                            SearchResultCardData searchResultCardData = new SearchResultCardData();
                            searchResultCardData.setAirportFrom(flight.getFrom().getAirportCode());
                            searchResultCardData.setAirportTo(flight.getTo().getAirportCode());
                            searchResultCardData.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                                    .plus(Duration.between(flight.getDepartureDateTime(),
                                            flight.getArrivalDateTime())));
                            searchResultCardData.setDepartureDateTime(flight.getDepartureDateTime());
                            searchResultCardData.setArrivalDateTime(flight.getArrivalDateTime());
                            searchResultCard.setDataTo(searchResultCardData);

                            SearchResultCardData searchResultCardDataBack = new SearchResultCardData();
                            searchResultCardDataBack.setAirportFrom(returnFlight.getFrom().getAirportCode());
                            searchResultCardDataBack.setAirportTo(returnFlight.getTo().getAirportCode());
                            searchResultCardDataBack.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                                    .plus(Duration.between(returnFlight.getDepartureDateTime(),
                                            returnFlight.getArrivalDateTime())));
                            searchResultCardDataBack.setDepartureDateTime(returnFlight.getDepartureDateTime());
                            searchResultCardDataBack.setArrivalDateTime(returnFlight.getArrivalDateTime());
                            searchResultCard.setDataBack(searchResultCardDataBack);

                            searchResultCardList.add(searchResultCard);
                        }
                    }
                } else {
                    SearchResultCardData searchResultCardData = new SearchResultCardData();
                    searchResultCardData.setAirportFrom(flight.getFrom().getAirportCode());
                    searchResultCardData.setAirportTo(flight.getTo().getAirportCode());
                    searchResultCardData.setFlightTime(LocalDateTime.of(0, 1, 1, 0, 0)
                            .plus(Duration.between(flight.getDepartureDateTime(),
                                    flight.getArrivalDateTime())));
                    searchResultCardData.setDepartureDateTime(flight.getDepartureDateTime());
                    searchResultCardData.setArrivalDateTime(flight.getArrivalDateTime());
                    searchResultCard.setDataTo(searchResultCardData);

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
