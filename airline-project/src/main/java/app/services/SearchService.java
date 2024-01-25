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

        // Разделение продолжительности на дни, часы и минуты
        long days = durationMinutes / (60 * 24);
        long hours = (durationMinutes % (60 * 24)) / 60;
        long minutes = durationMinutes % 60;

        String flightTime = "";
        if (days > 0) {
            flightTime += days + "д ";
        }
        if (hours > 0) {
            flightTime += hours + "ч ";
        }
        if (minutes > 0) {
            flightTime += minutes + "м";
        }

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
        List<FlightSeat> flightSeats = new ArrayList<>(flightSeatService.getSetFlightSeatsByFlightId(flight.getId()));
        flightSeats.sort(Comparator.comparingInt(FlightSeat::getFare));

        List<FlightSeat> sortedFlightSeats =
                flightSeats.subList(0, Math.min(search.getNumberOfPassengers(), flightSeats.size()));

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

                if (search.getReturnDate() != null) {
                    for (Flight returnFlight : returnFlights) {
                        SearchResultCard searchResultCard = new SearchResultCard();
                        SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                        searchResultCard.setDataTo(searchResultCardData);
                        Integer totalPriceDepart = findLowestFare(search, departFlight);
                        searchResultCard.setTotalPrice(totalPriceDepart);
                        foundSuitableReturnFlight = false;

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
                    SearchResultCard searchResultCard = new SearchResultCard();
                    SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                    searchResultCard.setDataTo(searchResultCardData);
                    Integer totalPriceDepart = findLowestFare(search, departFlight);
                    searchResultCard.setTotalPrice(totalPriceDepart);
                    searchResultCardList.add(searchResultCard);
                }
            }
        }
           Set<SearchResultCard> uniqueCards = new LinkedHashSet<>(searchResultCardList);
        searchResultCardList = new ArrayList<>(uniqueCards);
        return searchResultCardList;
    }
    @Loggable
    private boolean checkFlightForNumberSeats(Flight f, Search search) {
        return (flightSeatService.getNumberOfFreeSeatOnFlight(f) - search.getNumberOfPassengers()) >= 0;
    }
}

