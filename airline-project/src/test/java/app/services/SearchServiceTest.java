package app.services;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.dto.search.SearchResultCard;
import app.entities.Aircraft;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.enums.Airport;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private FlightService flightService;
    @Mock
    private DestinationService destinationService;
    @Mock
    private FlightSeatService flightSeatService;
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

        var listDirectFlight = List.of(directDepartureFlight);

        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        doReturn(new ArrayList<Flight>()).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(5).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
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

        var listDirectFlight = List.of(
                directDepartureFlight1, directDepartureFlight2, directDepartureFlight3
        );
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        doReturn(new ArrayList<Flight>()).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
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

        doReturn(new ArrayList<Flight>()).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
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

        doReturn(new ArrayList<Flight>()).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        List<SearchResultCard> flights = result.getFlights();
        assertEquals(4, result.getFlights().size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertNotNull(flight2.getDataTo());
        assertEquals(directReturnFlight2.getDepartureDateTime(), flight2.getDataBack().getDepartureDateTime());
        assertEquals(directReturnFlight2.getArrivalDateTime(), flight2.getDataBack().getArrivalDateTime());
        assertNotNull(flight2.getDataBack());
    }

    @DisplayName("5 search(), Positive test search 1 nondirect depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithOneNonDirectDepartFlightsAndWithoutReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = new ArrayList<Flight>();
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        List<SearchResultCard> flights = result.getFlights();
        assertEquals(2, flights.size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(nonDirectDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertNotNull(flight2.getDataTo());
        assertEquals(nonDirectDepartureFlight2.getDepartureDateTime(), flight2.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight2.getArrivalDateTime(), flight2.getDataTo().getArrivalDateTime());
    }

    @DisplayName("6 search(), Positive test search 2 nondirect depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithTwoNonDirectDepartFlightsAndWithoutReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Destination throughNizhni = new Destination();
        throughNizhni.setId(20L);
        throughNizhni.setAirportCode(Airport.GOJ);
        throughNizhni.setCityName("Нижний Новгород");
        throughNizhni.setTimezone("GMT +3");
        throughNizhni.setCountryName("Россия");
        throughNizhni.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);

        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(3L);
        Flight nonDirectDepartureFlight3 = new Flight();
        nonDirectDepartureFlight3.setId(3L);
        nonDirectDepartureFlight3.setCode("VKOGOJ");
        nonDirectDepartureFlight3.setFrom(fromVnukovo);
        nonDirectDepartureFlight3.setTo(throughNizhni);
        nonDirectDepartureFlight3.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight3.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 6, 0, 0)
        );
        nonDirectDepartureFlight3.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight3.setAircraft(aircraft3);
        nonDirectDepartureFlight3.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectDepartureFlight4 = new Flight();
        nonDirectDepartureFlight4.setId(4L);
        nonDirectDepartureFlight4.setCode("GOJSVX");
        nonDirectDepartureFlight4.setFrom(throughNizhni);
        nonDirectDepartureFlight4.setTo(toKoltcovo);
        nonDirectDepartureFlight4.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        nonDirectDepartureFlight4.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        nonDirectDepartureFlight4.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight4.setAircraft(aircraft4);
        nonDirectDepartureFlight4.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = new ArrayList<Flight>();
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(
                nonDirectDepartureFlight1,
                nonDirectDepartureFlight2,
                nonDirectDepartureFlight3,
                nonDirectDepartureFlight4
        );
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        List<SearchResultCard> flights = result.getFlights();
        assertEquals(4, flights.size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(nonDirectDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertNotNull(flight2.getDataTo());
        assertEquals(nonDirectDepartureFlight2.getDepartureDateTime(), flight2.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight2.getArrivalDateTime(), flight2.getDataTo().getArrivalDateTime());
    }

    @DisplayName("7 search(), Positive test search 1 nondirect and 1 direct depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithOneNonDirectAndOneDirectDepartFlightsAndWithoutReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight1);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        List<SearchResultCard> flights = result.getFlights();
        assertEquals(3, flights.size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertNotNull(flight2.getDataTo());
        assertEquals(nonDirectDepartureFlight1.getDepartureDateTime(), flight2.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight1.getArrivalDateTime(), flight2.getDataTo().getArrivalDateTime());


    }

    @DisplayName("8 search(), Positive test search 2 nondirect and 2 direct depart flight and 0 return flights")
    @Test
    public void shouldReturnSearchResultWithTwoNonDirectAndTwoDirectDepartFlightsAndWithoutReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Destination throughNizhni = new Destination();
        throughNizhni.setId(20L);
        throughNizhni.setAirportCode(Airport.GOJ);
        throughNizhni.setCityName("Нижний Новгород");
        throughNizhni.setTimezone("GMT +3");
        throughNizhni.setCountryName("Россия");
        throughNizhni.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(3L);
        Flight nonDirectDepartureFlight3 = new Flight();
        nonDirectDepartureFlight3.setId(3L);
        nonDirectDepartureFlight3.setCode("VKOGOJ");
        nonDirectDepartureFlight3.setFrom(fromVnukovo);
        nonDirectDepartureFlight3.setTo(throughNizhni);
        nonDirectDepartureFlight3.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 30, 0)
        );
        nonDirectDepartureFlight3.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 50, 0)
        );
        nonDirectDepartureFlight3.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight3.setAircraft(aircraft3);
        nonDirectDepartureFlight3.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectDepartureFlight4 = new Flight();
        nonDirectDepartureFlight4.setId(4L);
        nonDirectDepartureFlight4.setCode("GOJSVX");
        nonDirectDepartureFlight4.setFrom(throughNizhni);
        nonDirectDepartureFlight4.setTo(toKoltcovo);
        nonDirectDepartureFlight4.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 6, 0, 0)
        );
        nonDirectDepartureFlight4.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 7, 0, 0)
        );
        nonDirectDepartureFlight4.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight4.setAircraft(aircraft4);
        nonDirectDepartureFlight4.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(5L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft5);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft6 = new Aircraft();
        aircraft6.setId(5L);
        Flight directDepartureFlight2 = new Flight();
        directDepartureFlight2.setId(6L);
        directDepartureFlight2.setCode("VKOSVX");
        directDepartureFlight2.setFrom(fromVnukovo);
        directDepartureFlight2.setTo(toKoltcovo);
        directDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 14, 0, 0)
        );
        directDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 15, 0, 0)
        );
        directDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight2.setAircraft(aircraft6);
        directDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight1, directDepartureFlight2);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(
                nonDirectDepartureFlight1,
                nonDirectDepartureFlight2,
                nonDirectDepartureFlight3,
                nonDirectDepartureFlight4);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        List<SearchResultCard> flights = result.getFlights();
        assertEquals(6, flights.size());

        SearchResultCard flight1 = flights.get(0);
        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertNotNull(flight2.getDataTo());
        assertEquals(directDepartureFlight2.getDepartureDateTime(), flight2.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight2.getArrivalDateTime(), flight2.getDataTo().getArrivalDateTime());

        SearchResultCard flight3 = flights.get(2);
        assertEquals(nonDirectDepartureFlight1.getDepartureDateTime(), flight3.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight1.getArrivalDateTime(), flight3.getDataTo().getArrivalDateTime());
        assertNotNull(flight3.getDataTo());

        SearchResultCard flight4 = flights.get(3);
        assertEquals(nonDirectDepartureFlight2.getDepartureDateTime(), flight4.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight2.getArrivalDateTime(), flight4.getDataTo().getArrivalDateTime());
        assertNotNull(flight4.getDataTo());
    }

    @DisplayName("9 search(), Positive test search 1 nondirect " +
            "and 1 direct depart flight and 1 nondirect return flights")
    @Test
    public void shouldReturnSearchResultWithOneNonDirectAndOneDirectDepartFlightsAndOneNonDirectReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
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
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectReturnFlight1 = new Flight();
        nonDirectReturnFlight1.setId(4L);
        nonDirectReturnFlight1.setCode("SVXKZN");
        nonDirectReturnFlight1.setFrom(toKoltcovo);
        nonDirectReturnFlight1.setTo(throughKazan);
        nonDirectReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 4, 0, 0)
        );
        nonDirectReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        nonDirectReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight1.setAircraft(aircraft4);
        nonDirectReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight nonDirectReturnFlight2 = new Flight();
        nonDirectReturnFlight2.setId(5L);
        nonDirectReturnFlight2.setCode("KZNVKO");
        nonDirectReturnFlight2.setFrom(throughKazan);
        nonDirectReturnFlight2.setTo(fromVnukovo);
        nonDirectReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        nonDirectReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        nonDirectReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight2.setAircraft(aircraft5);
        nonDirectReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight1);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        var listDirectReturnFlight = new ArrayList<Flight>();
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        var listNonDirectReturnFlight = List.of(nonDirectReturnFlight1, nonDirectReturnFlight2);
        doReturn(listNonDirectReturnFlight)
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                        toKoltcovo.getId().intValue(),
                        fromVnukovo.getId().intValue(),
                        Date.valueOf(search.getReturnDate())
                );

        var listOfAllReturnFlights = new ArrayList<Flight>() {{
            addAll(listDirectReturnFlight);
            addAll(listNonDirectReturnFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        assertEquals(5, result.getFlights().size());
        List<SearchResultCard> flights = result.getFlights();

        SearchResultCard flight1 = flights.get(0);
        assertEquals(nonDirectDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(nonDirectDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());

    }

    @DisplayName("10 search(), Positive test search 1 nondirect and 1 direct depart flight " +
            "and 1 nondirect and 1 direct return flights")
    @Test
    public void shouldReturnSearchResultWithOneNonDirectAndDirectDepartFlightsAndOneNonDirectAndDirectReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectReturnFlight1 = new Flight();
        nonDirectReturnFlight1.setId(4L);
        nonDirectReturnFlight1.setCode("SVXKZN");
        nonDirectReturnFlight1.setFrom(toKoltcovo);
        nonDirectReturnFlight1.setTo(throughKazan);
        nonDirectReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 4, 0, 0)
        );
        nonDirectReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        nonDirectReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight1.setAircraft(aircraft4);
        nonDirectReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight nonDirectReturnFlight2 = new Flight();
        nonDirectReturnFlight2.setId(5L);
        nonDirectReturnFlight2.setCode("KZNVKO");
        nonDirectReturnFlight2.setFrom(throughKazan);
        nonDirectReturnFlight2.setTo(fromVnukovo);
        nonDirectReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        nonDirectReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        nonDirectReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight2.setAircraft(aircraft5);
        nonDirectReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft6 = new Aircraft();
        aircraft6.setId(6L);
        Flight directReturnFlight1 = new Flight();
        directReturnFlight1.setId(6L);
        directReturnFlight1.setCode("SVXVKO");
        directReturnFlight1.setFrom(toKoltcovo);
        directReturnFlight1.setTo(fromVnukovo);
        directReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        directReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        directReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight1.setAircraft(aircraft6);
        directReturnFlight1.setSeats(new ArrayList<FlightSeat>());


        var listDirectDepartFlight = List.of(directDepartureFlight1);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        var listDirectReturnFlight = List.of(directReturnFlight1);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        var listNonDirectReturnFlight = List.of(nonDirectReturnFlight1, nonDirectReturnFlight2);
        doReturn(listNonDirectReturnFlight)
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                        toKoltcovo.getId().intValue(),
                        fromVnukovo.getId().intValue(),
                        Date.valueOf(search.getReturnDate())
                );

        var listOfAllReturnFlights = new ArrayList<Flight>() {{
            addAll(listDirectReturnFlight);
            addAll(listNonDirectReturnFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));


        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        //expected 6
        assertEquals(7, result.getFlights().size());
        List<SearchResultCard> flights = result.getFlights();

        SearchResultCard flight1 = flights.get(0);
        assertEquals(directDepartureFlight1.getDepartureDateTime(), flight1.getDataTo().getDepartureDateTime());
        assertEquals(directDepartureFlight1.getArrivalDateTime(), flight1.getDataTo().getArrivalDateTime());
        assertNotNull(flight1.getDataTo());

        SearchResultCard flight2 = flights.get(1);
        assertEquals(nonDirectReturnFlight2.getDepartureDateTime(), flight2.getDataBack().getDepartureDateTime());
        assertEquals(nonDirectReturnFlight2.getArrivalDateTime(), flight2.getDataBack().getArrivalDateTime());
        assertNotNull(flight2.getDataBack());

        SearchResultCard flight3 = flights.get(2);
        assertEquals(directReturnFlight1.getDepartureDateTime(), flight3.getDataBack().getDepartureDateTime());
        assertEquals(directReturnFlight1.getArrivalDateTime(), flight3.getDataBack().getArrivalDateTime());
        assertNotNull(flight2.getDataBack());
    }

    @DisplayName("11 search(), Negative test search depart flight whithout return flights, but return nothing")
    @Test
    public void shouldReturnSearchResultWithEmptyDepartFlightListAndReturnFlightList() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(1);

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

        var listNonDirectDepartFlight = new ArrayList<Flight>();
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        assertEquals(0, result.getFlights().size());
        assertEquals(listOfAllDepartFlights.size(), result.getFlights().size());
        assertEquals(0, result.getFlights().size());
    }

    @DisplayName("12 search(), Negative test search depart and return flights, but return nothing")
    @Test
    public void shouldReturnSearchResultWithoutDepartAndReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);

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

        var listNonDirectDepartFlight = new ArrayList<Flight>();
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        var listOfAllReturnFlights = new ArrayList<Flight>() {{
            addAll(listDirectReturnFlight);

        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );

        assertEquals(0, result.getFlights().size());
        assertEquals(listOfAllDepartFlights.size(), result.getFlights().size());

    }

    @DisplayName("13 search(), Negative test search 1 nondirect and 1 direct depart flight" +
            " and 1 nondirect and 1 direct return flights but return nothing because there are no seats")
    @Test
    public void shouldReturnNothingBecauseThereAreNoSeatsOnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectReturnFlight1 = new Flight();
        nonDirectReturnFlight1.setId(4L);
        nonDirectReturnFlight1.setCode("SVXKZN");
        nonDirectReturnFlight1.setFrom(toKoltcovo);
        nonDirectReturnFlight1.setTo(throughKazan);
        nonDirectReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 4, 0, 0)
        );
        nonDirectReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        nonDirectReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight1.setAircraft(aircraft4);
        nonDirectReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight nonDirectReturnFlight2 = new Flight();
        nonDirectReturnFlight2.setId(5L);
        nonDirectReturnFlight2.setCode("KZNVKO");
        nonDirectReturnFlight2.setFrom(throughKazan);
        nonDirectReturnFlight2.setTo(fromVnukovo);
        nonDirectReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        nonDirectReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        nonDirectReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight2.setAircraft(aircraft5);
        nonDirectReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft6 = new Aircraft();
        aircraft6.setId(6L);
        Flight directReturnFlight1 = new Flight();
        directReturnFlight1.setId(6L);
        directReturnFlight1.setCode("SVXVKO");
        directReturnFlight1.setFrom(toKoltcovo);
        directReturnFlight1.setTo(fromVnukovo);
        directReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 3, 50, 0)
        );
        directReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 10, 0)
        );
        directReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight1.setAircraft(aircraft6);
        directReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight1);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listDirectReturnFlight = List.of(directReturnFlight1);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );


        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        assertEquals(0, result.getFlights().size());
    }

    @DisplayName("14 search(), Positive-Negative test search 1 nondirect and 1 direct depart flight " +
            "and 1 nondirect and 1 direct return flights but return only depart flights" +
            " and no return flights because there are no seats")
    @Test
    public void shouldReturnSearchResultWithOnlyDepartFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(1);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectReturnFlight1 = new Flight();
        nonDirectReturnFlight1.setId(4L);
        nonDirectReturnFlight1.setCode("SVXKZN");
        nonDirectReturnFlight1.setFrom(toKoltcovo);
        nonDirectReturnFlight1.setTo(throughKazan);
        nonDirectReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 4, 0, 0)
        );
        nonDirectReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        nonDirectReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight1.setAircraft(aircraft4);
        nonDirectReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight nonDirectReturnFlight2 = new Flight();
        nonDirectReturnFlight2.setId(5L);
        nonDirectReturnFlight2.setCode("KZNVKO");
        nonDirectReturnFlight2.setFrom(throughKazan);
        nonDirectReturnFlight2.setTo(fromVnukovo);
        nonDirectReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        nonDirectReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        nonDirectReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight2.setAircraft(aircraft5);
        nonDirectReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft6 = new Aircraft();
        aircraft6.setId(6L);
        Flight directReturnFlight1 = new Flight();
        directReturnFlight1.setId(6L);
        directReturnFlight1.setCode("SVXVKO");
        directReturnFlight1.setFrom(toKoltcovo);
        directReturnFlight1.setTo(fromVnukovo);
        directReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 3, 50, 0)
        );
        directReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 10, 0)
        );
        directReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight1.setAircraft(aircraft6);
        directReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        anyInt(),
                        anyInt(),
                        any(Date.class)
                );

        var listDirectDepartFlight = List.of(directDepartureFlight1);
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), any(Date.class)
        );

        var listOfAllDepartFlights = new ArrayList<Flight>() {{
            addAll(listDirectDepartFlight);
            addAll(listNonDirectDepartFlight);
        }};

        var listDirectReturnFlight = List.of(directReturnFlight1);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        var listNonDirectReturnFlight = List.of(nonDirectReturnFlight1, nonDirectReturnFlight2);
        doReturn(listNonDirectReturnFlight)
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                        toKoltcovo.getId().intValue(),
                        fromVnukovo.getId().intValue(),
                        Date.valueOf(search.getReturnDate())
                );


        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectDepartureFlight1);
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectDepartureFlight2);
        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(directDepartureFlight1);
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectReturnFlight1);
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectReturnFlight2);
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(directReturnFlight1);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        assertEquals(3, result.getFlights().size());
    }

    @DisplayName("15 search(), Positive-Negative test search 1 nondirect and 1 direct depart flight " +
            "and 1 nondirect and 1 direct return flights but return only return flights" +
            " and no depart flights because there are no seats")
    @Test
    public void shouldReturnSearchResultWithOnlyReturnFlights() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(LocalDate.of(2023, 4, 2));
        search.setNumberOfPassengers(2);

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

        Destination throughKazan = new Destination();
        throughKazan.setId(13L);
        throughKazan.setAirportCode(Airport.KZN);
        throughKazan.setCityName("Казань");
        throughKazan.setTimezone("GMT +3");
        throughKazan.setCountryName("Россия");
        throughKazan.setIsDeleted(false);

        Aircraft aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        Flight nonDirectDepartureFlight1 = new Flight();
        nonDirectDepartureFlight1.setId(1L);
        nonDirectDepartureFlight1.setCode("VKOKZN");
        nonDirectDepartureFlight1.setFrom(fromVnukovo);
        nonDirectDepartureFlight1.setTo(throughKazan);
        nonDirectDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        nonDirectDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );
        nonDirectDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight1.setAircraft(aircraft1);
        nonDirectDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        Flight nonDirectDepartureFlight2 = new Flight();
        nonDirectDepartureFlight2.setId(2L);
        nonDirectDepartureFlight2.setCode("KZNSVX");
        nonDirectDepartureFlight2.setFrom(throughKazan);
        nonDirectDepartureFlight2.setTo(toKoltcovo);
        nonDirectDepartureFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 4, 0, 0)
        );
        nonDirectDepartureFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 5, 0, 0)
        );
        nonDirectDepartureFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectDepartureFlight2.setAircraft(aircraft2);
        nonDirectDepartureFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft3 = new Aircraft();
        aircraft3.setId(2L);
        Flight directDepartureFlight1 = new Flight();
        directDepartureFlight1.setId(3L);
        directDepartureFlight1.setCode("VKOSVX");
        directDepartureFlight1.setFrom(fromVnukovo);
        directDepartureFlight1.setTo(toKoltcovo);
        directDepartureFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 9, 0, 0)
        );
        directDepartureFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 10, 0, 0)
        );
        directDepartureFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight1.setAircraft(aircraft3);
        directDepartureFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft4 = new Aircraft();
        aircraft4.setId(4L);
        Flight nonDirectReturnFlight1 = new Flight();
        nonDirectReturnFlight1.setId(4L);
        nonDirectReturnFlight1.setCode("SVXKZN");
        nonDirectReturnFlight1.setFrom(toKoltcovo);
        nonDirectReturnFlight1.setTo(throughKazan);
        nonDirectReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 4, 0, 0)
        );
        nonDirectReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 0, 0)
        );
        nonDirectReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight1.setAircraft(aircraft4);
        nonDirectReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft5 = new Aircraft();
        aircraft5.setId(5L);
        Flight nonDirectReturnFlight2 = new Flight();
        nonDirectReturnFlight2.setId(5L);
        nonDirectReturnFlight2.setCode("KZNVKO");
        nonDirectReturnFlight2.setFrom(throughKazan);
        nonDirectReturnFlight2.setTo(fromVnukovo);
        nonDirectReturnFlight2.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 8, 0, 0)
        );
        nonDirectReturnFlight2.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 9, 0, 0)
        );
        nonDirectReturnFlight2.setFlightStatus(FlightStatus.COMPLETED);
        nonDirectReturnFlight2.setAircraft(aircraft5);
        nonDirectReturnFlight2.setSeats(new ArrayList<FlightSeat>());

        Aircraft aircraft6 = new Aircraft();
        aircraft6.setId(6L);
        Flight directReturnFlight1 = new Flight();
        directReturnFlight1.setId(6L);
        directReturnFlight1.setCode("SVXVKO");
        directReturnFlight1.setFrom(toKoltcovo);
        directReturnFlight1.setTo(fromVnukovo);
        directReturnFlight1.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 2, 3, 50, 0)
        );
        directReturnFlight1.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 2, 5, 10, 0)
        );
        directReturnFlight1.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight1.setAircraft(aircraft6);
        directReturnFlight1.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight1);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        var listNonDirectDepartFlight = List.of(nonDirectDepartureFlight1, nonDirectDepartureFlight2);
        doReturn(listNonDirectDepartFlight).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        var listDirectReturnFlight = List.of(directReturnFlight1);
        var returnDate = Date.valueOf(search.getReturnDate());
        doReturn(listDirectReturnFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(returnDate)
        );

        var listNonDirectReturnFlight = List.of(nonDirectReturnFlight1, nonDirectReturnFlight2);

        var listOfAllReturnFlights = new ArrayList<Flight>() {{
            addAll(listDirectReturnFlight);
            addAll(listNonDirectReturnFlight);
        }};

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectDepartureFlight1);
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(nonDirectDepartureFlight2);
        doReturn(0).when(flightSeatService).getNumberOfFreeSeatOnFlight(directDepartureFlight1);

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        //expected 3!
        assertEquals(0, result.getFlights().size());

    }

    @DisplayName("16 (calculate fare), finds the lowest price for a seat")
    @Test
    public void shouldReturnLowerFare() {
        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(LocalDate.of(2023, 4, 1));
        search.setReturnDate(null);
        search.setNumberOfPassengers(2);

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

        FlightSeat seat1 = new FlightSeat();
        seat1.setFare(200);

        FlightSeat seat2 = new FlightSeat();
        seat2.setFare(100);

        FlightSeat seat3 = new FlightSeat();
        seat3.setFare(50);

        Set<FlightSeat> flightSeats = new HashSet<>();
        flightSeats.add(seat1);
        flightSeats.add(seat2);
        flightSeats.add(seat3);

        var listDirectFlight = List.of(directDepartureFlight);

        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
                any(Airport.class), any(Airport.class), eq(departureDate)
        );

        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
        doReturn(5).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));
        doReturn(flightSeats).when(flightSeatService).getSetFlightSeatsByFlightId(directDepartureFlight.getId());

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );
        //expected lowest fare = 50
        Integer lowestFare = searchService.findLowestFare(search, directDepartureFlight);
        assertEquals(200, lowestFare);
    }
}


