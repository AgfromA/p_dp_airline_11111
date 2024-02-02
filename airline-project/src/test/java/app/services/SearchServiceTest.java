package app.services;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.dto.search.SearchResultCard;
import app.entities.Aircraft;
import app.entities.Category;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.enums.Airport;
import app.enums.CategoryType;
import app.enums.FlightStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private FlightService flightService;
    @Mock
    private FlightSeatService flightSeatService;
    @Mock
    private SeatService seatService;

    @InjectMocks
    private SearchService searchService;

    @DisplayName("1 search(), Positive test search 1 direct depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithOneDirectDepartFlightsWithoutReturnFlight() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);

        FlightSeat seat2 = new FlightSeat();
        seat2.setFare(100);

        Set<FlightSeat> flightSeats = new HashSet<>();
        flightSeats.add(seat1);
        flightSeats.add(seat2);

        Category category = new Category();
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight.setAircraft(aircraft1);
        directDepartureFlight.setSeats(new ArrayList<FlightSeat>());

        var listDirectFlight = List.of(directDepartureFlight);

        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        doReturn(5).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));
        doReturn(List.of(3l, 4l)).when(flightSeatService).findSeatIdsByFlight(directDepartureFlight);
        doReturn(seats).when(seatService).findByAircraftId(1L);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );

        assertEquals(1, result.getFlights().size());
        assertEquals(listDirectFlight.size(), result.getFlights().size());
        for (int i = 0; i < listDirectFlight.size(); i++) {
            assertEquals(listDirectFlight.get(i).getDepartureDateTime(), result.getFlights().get(i).getDataTo().getDepartureDateTime());
            assertEquals(listDirectFlight.get(i).getArrivalDateTime(), result.getFlights().get(i).getDataTo().getArrivalDateTime());
            assertNotNull(result.getFlights().get(i).getDataTo());
        }
    }

    @DisplayName("2 search(), Positive test search 3 direct depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithThreeDirectDepartFlightsWithoutReturnFlight() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(1L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft1);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight directDepartureFlight2 = new Flight();
        directDepartureFlight2.setId(2L);
        directDepartureFlight2.setCode("VKOSVX");
        directDepartureFlight2.setFrom(fromVnukovo);
        directDepartureFlight2.setTo(toKoltcovo);
        directDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 20, 0)
        );
        directDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 3, 30, 0)
        );
        directDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight2.setAircraft(aircraft2);
        directDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(3L);
        Flight directDepartureFlight3 = new Flight();
        directDepartureFlight3.setId(3L);
        directDepartureFlight3.setCode("VKOSVX");
        directDepartureFlight3.setFrom(fromVnukovo);
        directDepartureFlight3.setTo(toKoltcovo);
        directDepartureFlight3.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 6, 55, 0)
        );
        directDepartureFlight3.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 8, 10, 0)
        );
        directDepartureFlight3.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight3.setAircraft(aircraft3);
        directDepartureFlight3.setSeats(new ArrayList<FlightSeat>());

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setFare(100);

        Set<FlightSeat> flightSeats = new HashSet<>();
        flightSeats.add(seat1);
        flightSeats.add(flightSeat2);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryType(CategoryType.ECONOMY);

        Category category3 = new Category();
        category3.setId(3L);
        category3.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setCategory(category2);
        seat2.setAircraft(aircraft2);

        Set<Seat> seats2 = new HashSet<>();
        seats2.add(seat2);

        Seat seat3 = new Seat();
        seat3.setId(3L);
        seat3.setCategory(category3);
        seat3.setAircraft(aircraft3);

        Set<Seat> seats3 = new HashSet<>();
        seats3.add(seat3);

        var listDirectFlight = List.of(
                directDepartureFlight1, directDepartureFlight2, directDepartureFlight3
        );
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));
        doReturn(List.of(1l, 2l)).when(flightSeatService).findSeatIdsByFlight(any(Flight.class));
        doReturn(seats).when(seatService).findByAircraftId(1L);
        doReturn(seats2).when(seatService).findByAircraftId(2L);
        doReturn(seats3).when(seatService).findByAircraftId(3L);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );

        assertEquals(3, result.getFlights().size());
        assertEquals(listDirectFlight.size(), result.getFlights().size());
        for (int i = 0; i < listDirectFlight.size(); i++) {
            assertEquals(listDirectFlight.get(i).getDepartureDateTime(), result.getFlights().get(i).getDataTo().getDepartureDateTime());
            assertEquals(listDirectFlight.get(i).getArrivalDateTime(), result.getFlights().get(i).getDataTo().getArrivalDateTime());
            assertNotNull(result.getFlights().get(i).getDataTo());
        }
    }

    @DisplayName("3 search(), Positive test search 1 direct depart flight and 1 direct return flights")
    @Test
    public void shouldReturnSearchResultWithOneDirectDepartFlightAndOneDirectReturnFlight() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight.setAircraft(aircraft1);
        directDepartureFlight.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(1L);
        Flight directReturnFlight = new Flight();
        directReturnFlight.setId(2L);
        directReturnFlight.setCode("SVXVKO");
        directReturnFlight.setFrom(toKoltcovo);
        directReturnFlight.setTo(fromVnukovo);
        directReturnFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        directReturnFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 6, 0, 0)
        );
        directReturnFlight.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight.setAircraft(aircraft2);
        directReturnFlight.setSeats(new ArrayList<FlightSeat>());

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setFare(100);

        Set<FlightSeat> flightSeats = new HashSet<>();
        flightSeats.add(seat1);
        flightSeats.add(flightSeat2);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        var listDirectDepartFlight = List.of(directDepartureFlight);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listDirectReturnFlight = List.of(directReturnFlight);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));
        doReturn(List.of(3l, 4l)).when(flightSeatService).findSeatIdsByFlight(directDepartureFlight);
        doReturn(List.of(5l, 6l)).when(flightSeatService).findSeatIdsByFlight(directReturnFlight);
        doReturn(seats).when(seatService).findByAircraftId(1L);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );

        assertEquals(1, result.getFlights().size());
        assertEquals(listDirectDepartFlight.size(), result.getFlights().size());
        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getFlights().get(i).getDataTo().getDepartureDateTime());
            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getFlights().get(i).getDataTo().getArrivalDateTime());
            assertNotNull(result.getFlights().get(i).getDataBack());
        }
        assertEquals(1, result.getFlights().size());
        assertEquals(listDirectReturnFlight.size(), result.getFlights().size());
        for (int i = 0; i < listDirectReturnFlight.size(); i++) {
            assertEquals(listDirectReturnFlight.get(i).getDepartureDateTime(), result.getFlights().get(i).getDataBack().getDepartureDateTime());
            assertEquals(listDirectReturnFlight.get(i).getArrivalDateTime(), result.getFlights().get(i).getDataBack().getArrivalDateTime());
            assertNotNull(result.getFlights().get(i).getDataBack());
        }
    }

    @DisplayName("4 search(), Positive test search 2 direct depart flight and 2 direct return flights")
    @Test
    public void shouldReturnSearchResultWithTwoDirectDepartFlightsAndTwoDirectReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(1L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft1);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight directDepartureFlight2 = new Flight();
        directDepartureFlight2.setId(2L);
        directDepartureFlight2.setCode("VKOSVX");
        directDepartureFlight2.setFrom(fromVnukovo);
        directDepartureFlight2.setTo(toKoltcovo);
        directDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 3, 0, 0)
        );
        directDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        directDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight2.setAircraft(aircraft2);
        directDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(3L);
        Flight directReturnFlight1 = new Flight();
        directReturnFlight1.setId(3L);
        directReturnFlight1.setCode("SVXVKO");
        directReturnFlight1.setFrom(toKoltcovo);
        directReturnFlight1.setTo(fromVnukovo);
        directReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        directReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 6, 0, 0)
        );
        directReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight1.setAircraft(aircraft3);
        directReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight directReturnFlight2 = new Flight();
        directReturnFlight2.setId(4L);
        directReturnFlight2.setCode("SVXVKO");
        directReturnFlight2.setFrom(toKoltcovo);
        directReturnFlight2.setTo(fromVnukovo);
        directReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 10, 0, 0)
        );
        directReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 11, 0, 0)
        );
        directReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight2.setAircraft(aircraft4);
        directReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setFare(100);

        Set<FlightSeat> flightSeats = new HashSet<>();
        flightSeats.add(seat1);
        flightSeats.add(flightSeat2);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setCategoryType(CategoryType.ECONOMY);

        Category category3 = new Category();
        category3.setId(3L);
        category3.setCategoryType(CategoryType.ECONOMY);

        Category category4 = new Category();
        category4.setId(4L);
        category4.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setCategory(category2);
        seat2.setAircraft(aircraft2);

        Set<Seat> seats2 = new HashSet<>();
        seats2.add(seat2);

        Seat seat3 = new Seat();
        seat3.setId(2L);
        seat3.setCategory(category3);
        seat3.setAircraft(aircraft3);

        Set<Seat> seats3 = new HashSet<>();
        seats3.add(seat3);

        Seat seat4 = new Seat();
        seat4.setId(2L);
        seat4.setCategory(category4);
        seat4.setAircraft(aircraft4);

        Set<Seat> seats4 = new HashSet<>();
        seats4.add(seat4);

        var listDirectDepartFlight = List.of(directDepartureFlight1, directDepartureFlight2);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listDirectReturnFlight = List.of(directReturnFlight1, directReturnFlight2);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));
        doReturn(List.of(5l, 6l)).when(flightSeatService).findSeatIdsByFlight(any(Flight.class));
        doReturn(seats).when(seatService).findByAircraftId(1L);
        doReturn(seats2).when(seatService).findByAircraftId(2L);
        doReturn(seats3).when(seatService).findByAircraftId(3L);
        doReturn(seats4).when(seatService).findByAircraftId(4L);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );
        List<SearchResultCard> flights = result.getFlights();
        assertEquals(4, result.getFlights().size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertEquals(directReturnFlight2.getDepartureDateTime(), flight2.getDataBack().getDepartureDateTime());
        assertEquals(directReturnFlight2.getArrivalDateTime(), flight2.getDataBack().getArrivalDateTime());
        assertNotNull(flight2.getDataBack());

        SearchResultCard flight3 = flights.get(2);
        assertEquals(directDepartureFlight2.getDepartureDateTime(), flight3.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight2.getArrivalDateTime(), flight3.getDataTo().getArrivalDateTime());
        assertNotNull(flight3.getDataTo());

        SearchResultCard flight4 = flights.get(0);
        assertEquals(directReturnFlight1.getDepartureDateTime(), flight4.getDataBack().getDepartureDateTime());
        assertEquals(directReturnFlight1.getArrivalDateTime(), flight4.getDataBack().getArrivalDateTime());
        assertNotNull(flight4.getDataTo());
    }

    @DisplayName("5 search(), Negative test search depart flight whithout return flights, but return nothing")
    @Test
    public void shouldReturnSearchResultWithEmptyDepartFlightListAndReturnFlightList() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        var listDirectDepartFlight = new ArrayList<Flight>();
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
        }};

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );

        assertEquals(0, result.getFlights().size());
        assertEquals(listOfAllDepartFlights.size(), result.getFlights().size());
    }

    @DisplayName("6 search(), Negative test search depart and return flights, but return nothing")
    @Test
    public void shouldReturnSearchResultWithoutDepartAndReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        var listDirectDepartFlight = new ArrayList<Flight>();
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listDirectReturnFlight = new ArrayList<Flight>();
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
        }};

        var listOfAllReturnFlights = new ArrayList<Flight>() {{
            addAll(listDirectReturnFlight);

        }};

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers(),
                search.getCategoryOfSeats()
        );
        assertEquals(0, result.getFlights().size());
        assertEquals(listOfAllDepartFlights.size(), result.getFlights().size());
    }

    @DisplayName("7 (calculate fare), finds the price for a seat for 1 passenger")
    @Test
    public void shouldReturnFareIfOnePassengers() {
        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.ON_TIME);
        directDepartureFlight.setAircraft(aircraft1);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);
        seat.setSeatNumber("1A");
        seat.setIsLockedBack(false);
        seat.setIsLockedBack(false);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        doReturn(seats).when(seatService).findByAircraftId(1L);
        doReturn(500).when(flightSeatService).generateFareForFlightSeat(seat, directDepartureFlight);

        Integer fareDepart1 = searchService.findFare(search, directDepartureFlight);

        assertEquals(500, fareDepart1);
    }

    @DisplayName("8 (calculate fare), finds the price for a seat for 2 passenger")
    @Test
    public void shouldReturnFareIfTwoPassengers() {
        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(2);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.ON_TIME);
        directDepartureFlight.setAircraft(aircraft1);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);
        seat.setSeatNumber("1A");
        seat.setIsLockedBack(false);
        seat.setIsLockedBack(false);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        doReturn(seats).when(seatService).findByAircraftId(1L);
        doReturn(500).when(flightSeatService).generateFareForFlightSeat(seat, directDepartureFlight);

        Integer fareDepart1 = searchService.findFare(search, directDepartureFlight);

        assertEquals(1000, fareDepart1);
    }

    @DisplayName("9 (check FlightSeatId), returns the expected seat IDs for a given flight")
    @Test
    void testFindFlightSeatIdsByFlight() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.ON_TIME);
        directDepartureFlight.setAircraft(aircraft1);

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);
        seat1.setId(2L);
        seat1.setFlight(directDepartureFlight);
        seat1.setIsBooked(false);
        seat1.setIsSold(false);
        seat1.setIsRegistered(false);

        FlightSeat seat2 = new FlightSeat();
        seat2.setFare(100);
        seat2.setId(3L);
        seat2.setFlight(directDepartureFlight);
        seat2.setIsBooked(false);
        seat2.setIsSold(false);
        seat2.setIsRegistered(false);

        Set<FlightSeat> flightSeats1 = new HashSet<>();
        flightSeats1.add(seat1);
        flightSeats1.add(seat2);

        when(flightSeatService.findSeatIdsByFlight(directDepartureFlight))
                .thenReturn(List.of(2L, 3L));

        List<Long> flightSeatId = flightSeatService.findSeatIdsByFlight(directDepartureFlight);

        Long firstFlightSeatId = flightSeatId.get(0);

        assertFalse(flightSeatId.isEmpty(), "Seat IDs list should not be empty");

        assertEquals(2L, firstFlightSeatId);
    }

    @DisplayName("10 (check NumberSeats), Positive test, be sure that seat is not booked,is not sold and is not registered")
    @Test
    void shouldFindFiveNumberOfFreeSeatOnFlight() {
        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(5);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.ON_TIME);
        directDepartureFlight.setAircraft(aircraft1);


        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);
        seat1.setId(2L);
        seat1.setFlight(directDepartureFlight);
        seat1.setIsBooked(false);
        seat1.setIsSold(false);
        seat1.setIsRegistered(false);

        FlightSeat seat2 = new FlightSeat();
        seat2.setFare(100);
        seat2.setId(3L);
        seat2.setFlight(directDepartureFlight);
        seat2.setIsBooked(false);
        seat2.setIsSold(false);
        seat2.setIsRegistered(false);

        Set<FlightSeat> flightSeats1 = new HashSet<>();
        flightSeats1.add(seat1);
        flightSeats1.add(seat2);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        when(flightSeatService.getNumberOfFreeSeatOnFlight(any(Flight.class))).thenReturn(6);
        doReturn(seats).when(seatService).findByAircraftId(1L);

        boolean result = searchService.checkFlightForNumberSeats(directDepartureFlight, search);
        assertTrue(result);
    }

    @DisplayName("11 (check NumberSeats), Negative test, be sure that seat is not booked,is not sold and is not registered")
    @Test
    void shouldFindTwoNumberOfFreeSeatOnFlight() {
        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(2);
        search.setCategoryOfSeats(CategoryType.ECONOMY);

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);
        fromVnukovo.setCityName("Москва");
        fromVnukovo.setTimezone("GMT +3");
        fromVnukovo.setCountryName("Россия");
        fromVnukovo.setIsDeleted(false);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(6L);
        toKoltcovo.setAirportCode(Airport.SVX);
        toKoltcovo.setCityName("Екатеринбург");
        toKoltcovo.setTimezone("GMT +5");
        toKoltcovo.setCountryName("Россия");
        toKoltcovo.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);

        Flight directDepartureFlight = new Flight();
        directDepartureFlight.setId(1L);
        directDepartureFlight.setCode("VKOSVX");
        directDepartureFlight.setFrom(fromVnukovo);
        directDepartureFlight.setTo(toKoltcovo);
        directDepartureFlight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        directDepartureFlight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        directDepartureFlight.setFlightStatus(FlightStatus.ON_TIME);
        directDepartureFlight.setAircraft(aircraft1);

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);
        seat1.setId(2L);
        seat1.setFlight(directDepartureFlight);
        seat1.setIsBooked(false);
        seat1.setIsSold(false);
        seat1.setIsRegistered(false);

        FlightSeat seat2 = new FlightSeat();
        seat2.setFare(100);
        seat2.setId(3L);
        seat2.setFlight(directDepartureFlight);
        seat2.setIsBooked(false);
        seat2.setIsSold(false);
        seat2.setIsRegistered(false);

        Set<FlightSeat> flightSeats1 = new HashSet<>();
        flightSeats1.add(seat1);
        flightSeats1.add(seat2);

        Category category = new Category();
        category.setId(1L);
        category.setCategoryType(CategoryType.ECONOMY);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setCategory(category);
        seat.setAircraft(aircraft1);

        Set<Seat> seats = new HashSet<>();
        seats.add(seat);

        when(flightSeatService.getNumberOfFreeSeatOnFlight(any(Flight.class))).thenReturn(1);
        doReturn(seats).when(seatService).findByAircraftId(1L);

        boolean result = searchService.checkFlightForNumberSeats(directDepartureFlight, search);
        assertFalse(result);
    }
}



