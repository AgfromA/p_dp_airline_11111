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
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final FlightService flightService;
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

        return searchResult;

    }
    @Loggable
    private SearchResultCardData builderForSearchResultCardData(Flight flight) {

        // Получение часовых поясов для аэропортов
        ZoneId departureTimeZone = parseTimeZone(flight.getFrom().getTimezone());
        ZoneId arrivalTimeZone = parseTimeZone(flight.getTo().getTimezone());

        // Преобразование времени в часовые пояса аэропортов
        ZonedDateTime departureDateTimeInTimeZone = flight.getDepartureDateTime().atZone(departureTimeZone);
        ZonedDateTime arrivalDateTimeInTimeZone = flight.getArrivalDateTime().atZone(arrivalTimeZone);

        // Вычисление продолжительности полета в минутах
        Duration duration = Duration.between(departureDateTimeInTimeZone, arrivalDateTimeInTimeZone);

        // Использование Math.abs() чтобы получить абсолютное значение
        long durationMinutes = Math.abs(duration.toMinutes());

        // Разделение продолжительности на часы и минуты
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;

        String flightTime = hours + "h " + minutes + "m";

        return SearchResultCardData.builder()
                .airportFrom(flight.getFrom().getAirportCode())
                .airportTo(flight.getTo().getAirportCode())
                .flightTime(flightTime)
                .departureDateTime(flight.getDepartureDateTime())
                .arrivalDateTime(flight.getArrivalDateTime())
                .build();
    }
    @Loggable
    public Integer findLowestFare(Search search, Flight flight) {
        Set<FlightSeat> flightSeats = new TreeSet<>(Comparator.comparingInt(FlightSeat::getFare));
        flightSeats.addAll(flightSeatService.getSetFlightSeatsByFlightId(flight.getId()));

        Set<FlightSeat> sortedFlightSeats = flightSeats.stream()
                .limit(search.getNumberOfPassengers())
                .collect(Collectors.toSet());

        Integer fare = 0;
        for (FlightSeat seat : sortedFlightSeats) {
            fare += seat.getFare();
        }
        return fare;
    }

    @Loggable
    private ZoneId parseTimeZone(String timeZone) {
        // Проверка, является ли timeZone в формате "GMT +XX"
        if (timeZone.startsWith("GMT")) {
            String offset = timeZone.substring(4).trim();
            ZoneOffset zoneOffset = ZoneOffset.of(offset);
            return ZoneId.from(zoneOffset);
        } else {
            return ZoneId.of(timeZone);
        }
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
        boolean foundSuitableReturnFlight = false;

        for (Flight departFlight : departFlights) {
            if (checkFlightForNumberSeats(departFlight, search)) {
                SearchResultCard searchResultCard = new SearchResultCard();
                SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                searchResultCard.setDataTo(searchResultCardData);
                Integer totalPriceDepart = findLowestFare(search, departFlight);
                searchResultCard.setTotalPrice(totalPriceDepart);

                if (search.getReturnDate() != null) {
                    foundSuitableReturnFlight = false;
                    for (Flight returnFlight : returnFlights) {
                        if (checkFlightForNumberSeats(returnFlight, search)) {
                            SearchResultCardData searchResultCardDataBack = builderForSearchResultCardData(returnFlight);
                            searchResultCard.setDataBack(searchResultCardDataBack);
                            Integer totalPriceReturn = totalPriceDepart + findLowestFare(search, returnFlight);
                            searchResultCard.setTotalPrice(totalPriceReturn);
                            searchResultCardList.add(searchResultCard);
                            foundSuitableReturnFlight = true;
                        }
                    }
                }
                if (!foundSuitableReturnFlight) {
                    searchResultCardList.add(searchResultCard);
                    searchResultCard.setTotalPrice(totalPriceDepart);
                }
            }
        }
        //   Set<SearchResultCard> uniqueCards = new LinkedHashSet<>(searchResultCardList);
       // searchResultCardList = new ArrayList<>(uniqueCards);

        return searchResultCardList;
    }
    @Loggable
    private boolean checkFlightForNumberSeats(Flight f, Search search) {
        return (flightSeatService.getNumberOfFreeSeatOnFlight(f) - search.getNumberOfPassengers()) >= 0;
    }
}

