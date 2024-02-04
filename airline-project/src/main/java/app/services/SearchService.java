package app.services;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.dto.search.SearchResultCard;
import app.dto.search.SearchResultCardData;
import app.entities.Flight;
import app.entities.Seat;
import app.enums.Airport;
import app.enums.CategoryType;
import app.utils.aop.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final FlightService flightService;
    private final FlightSeatService flightSeatService;
    private final SeatService seatService;

    @Transactional
    @Loggable
    public SearchResult search(Airport from, Airport to, LocalDate departureDate,
                               LocalDate returnDate, Integer numberOfPassengers, CategoryType categoryOfSeats) {

        var search = new Search(from, to, departureDate, returnDate, numberOfPassengers, categoryOfSeats);

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

        // Получение строки продолжительности времени полета в виде "дд чч мм"
        String flightTime = getFormattedFlightDurationString(duration);

        List<Long> flightSeatId = flightSeatService.findFlightSeatIdsByFlight(flight);

        Long firstFlightSeatId = flightSeatId.get(0);

        return SearchResultCardData.builder()
                .airportFrom(flight.getFrom().getAirportCode())
                .airportTo(flight.getTo().getAirportCode())
                .cityTo(flight.getTo().getCityName())
                .cityFrom(flight.getFrom().getCityName())
                .flightSeatId(firstFlightSeatId)
                .flightTime(flightTime)
                .departureDateTime(flight.getDepartureDateTime())
                .arrivalDateTime(flight.getArrivalDateTime())
                .build();
    }

    @Loggable
    private static String getFormattedFlightDurationString(Duration duration) {

        long durationMinutes = Math.abs(duration.toMinutes());

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
        return flightTime;
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
    // находим стоимость для каждого места в зависимости от полета, категории места, количества пассажиров
    public Integer findFare(Search search, Flight flight) {

        long aircraftId = flight.getAircraft().getId();

        Set<Seat> seats = seatService.findByAircraftId(aircraftId);

        int fare = 0;

        for (Seat seat : seats) {
            fare = flightSeatService.generateFareForFlightSeat(seat, flight);
        }
        return fare * search.getNumberOfPassengers();
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
        // Флаг для обозначения наличия подходящего возвратного рейса
        boolean foundSuitableReturnFlight = false;

        for (Flight departFlight : departFlights) {
            if (checkFlightForNumberSeats(departFlight, search)) {

                if (search.getReturnDate() != null) {
                    for (Flight returnFlight : returnFlights) {
                        SearchResultCard searchResultCard = new SearchResultCard();
                        SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                        searchResultCard.setDataTo(searchResultCardData);
                        Integer totalPriceDepart = findFare(search, departFlight);
                        searchResultCard.setTotalPrice(totalPriceDepart);
                        foundSuitableReturnFlight = false;

                        if (checkFlightForNumberSeats(returnFlight, search)) {
                            SearchResultCardData searchResultCardDataBack = builderForSearchResultCardData(returnFlight);
                            searchResultCard.setDataBack(searchResultCardDataBack);
                            Integer totalPriceReturn = totalPriceDepart + findFare(search, returnFlight);
                            searchResultCard.setTotalPrice(totalPriceReturn);
                            searchResultCardList.add(searchResultCard);
                            foundSuitableReturnFlight = true;

                        }
                    }
                }
                // Если не найден подходящий возвратный рейс, добавляем карточку результатов для направляющего рейса
                if (!foundSuitableReturnFlight) {
                    SearchResultCard searchResultCard = new SearchResultCard();
                    SearchResultCardData searchResultCardData = builderForSearchResultCardData(departFlight);
                    searchResultCard.setDataTo(searchResultCardData);
                    Integer totalPriceDepart = findFare(search, departFlight);
                    searchResultCard.setTotalPrice(totalPriceDepart);
                    searchResultCardList.add(searchResultCard);
                }
            }
        }
        Set<SearchResultCard> uniqueCards = new LinkedHashSet<>(searchResultCardList);
        searchResultCardList = new ArrayList<>(uniqueCards);
        return searchResultCardList;
    }

    //достаточно ли свободных мест в рейсе для указанного количества пассажиров и соответствует ли категория мест запросу.
    @Loggable
    public boolean checkFlightForNumberSeats(Flight flight, Search search) {
        int numberOfPassengers = search.getNumberOfPassengers();
        int numberOfFreeSeats = flightSeatService.getNumberOfFreeSeatOnFlight(flight);

        int count = 0;
        for (int i = 0; flight.getSeats().size() > i; i++) {
            if (flight.getSeats().get(i).getSeat().getCategory().getCategoryType().equals(search.getCategoryOfSeats())) {
                count++;
                if (count >= numberOfPassengers) {
                    return numberOfFreeSeats >= numberOfPassengers;
                }
            }
        }
        return false;
    }
}
