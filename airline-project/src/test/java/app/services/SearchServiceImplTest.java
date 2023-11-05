package app.services;

import app.dto.search.Search;
import app.dto.search.SearchResult;
import app.entities.Aircraft;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.interfaces.DestinationService;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImplTest {

    @Mock
    private FlightService flightService;
    @Mock
    private DestinationService destinationService;
    @Mock
    private FlightSeatService flightSeatService;
    @InjectMocks
    private SearchServiceImpl searchService;


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
        directDepartureFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
        directDepartureFlight.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight.setAircraft(aircraft1);
        directDepartureFlight.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
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

        assertEquals(listDirectDepartFlight.size(), 1);
        assertEquals(listDirectDepartFlight.size(), result.getDepartFlights().size());
        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
            assertEquals(listDirectDepartFlight.get(i).getId(), result.getDepartFlights().get(i).getId());
            assertEquals(listDirectDepartFlight.get(i).getCode(), result.getDepartFlights().get(i).getCode());
            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getDepartFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getDepartFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectDepartFlight.get(i).getFrom().getAirportCode(), result.getDepartFlights().get(i).getAirportFrom());
            assertEquals(listDirectDepartFlight.get(i).getTo().getAirportCode(), result.getDepartFlights().get(i).getAirportTo());
            assertEquals(listDirectDepartFlight.get(i).getFlightStatus(), result.getDepartFlights().get(i).getFlightStatus());
            assertEquals(listDirectDepartFlight.get(i).getAircraft().getId(), result.getDepartFlights().get(i).getAircraftId());

            assertEquals(0, result.getReturnFlights().size());
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
        directDepartureFlight1.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight1.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
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
        directDepartureFlight2.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 2, 20, 0));
        directDepartureFlight2.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 3, 30, 0));
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
        directDepartureFlight3.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 6, 55, 0));
        directDepartureFlight3.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 8, 10, 0));
        directDepartureFlight3.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight3.setAircraft(aircraft3);
        directDepartureFlight3.setSeats(new ArrayList<FlightSeat>());


        var listDirectDepartFlight = List.of(directDepartureFlight1, directDepartureFlight2, directDepartureFlight3);
        var departureDate = Date.valueOf(search.getDepartureDate());
        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
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

        assertEquals(listDirectDepartFlight.size(), 3);
        assertEquals(listDirectDepartFlight.size(), result.getDepartFlights().size());
        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
            assertEquals(listDirectDepartFlight.get(i).getId(), result.getDepartFlights().get(i).getId());
            assertEquals(listDirectDepartFlight.get(i).getCode(), result.getDepartFlights().get(i).getCode());
            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getDepartFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getDepartFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectDepartFlight.get(i).getFrom().getAirportCode(), result.getDepartFlights().get(i).getAirportFrom());
            assertEquals(listDirectDepartFlight.get(i).getTo().getAirportCode(), result.getDepartFlights().get(i).getAirportTo());
            assertEquals(listDirectDepartFlight.get(i).getFlightStatus(), result.getDepartFlights().get(i).getFlightStatus());
            assertEquals(listDirectDepartFlight.get(i).getAircraft().getId(), result.getDepartFlights().get(i).getAircraftId());

            assertEquals(0, result.getReturnFlights().size());
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
        directDepartureFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
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
        directReturnFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 2, 5, 0, 0));
        directReturnFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 2, 6, 0, 0));
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

        doReturn(new ArrayList<Flight>())
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                fromVnukovo.getId().intValue(),
                toKoltcovo.getId().intValue(),
                Date.valueOf(search.getReturnDate())
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

        assertEquals(listDirectDepartFlight.size(), 1);
        assertEquals(listDirectDepartFlight.size(), result.getDepartFlights().size());
        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
            assertEquals(listDirectDepartFlight.get(i).getId(), result.getDepartFlights().get(i).getId());
            assertEquals(listDirectDepartFlight.get(i).getCode(), result.getDepartFlights().get(i).getCode());
            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getDepartFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getDepartFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectDepartFlight.get(i).getFrom().getAirportCode(), result.getDepartFlights().get(i).getAirportFrom());
            assertEquals(listDirectDepartFlight.get(i).getTo().getAirportCode(), result.getDepartFlights().get(i).getAirportTo());
            assertEquals(listDirectDepartFlight.get(i).getFlightStatus(), result.getDepartFlights().get(i).getFlightStatus());
            assertEquals(listDirectDepartFlight.get(i).getAircraft().getId(), result.getDepartFlights().get(i).getAircraftId());
        }

        assertEquals(listDirectReturnFlight.size(), 1);
        assertEquals(listDirectReturnFlight.size(), result.getReturnFlights().size());
        for (int i = 0; i < listDirectReturnFlight.size(); i++) {
            assertEquals(listDirectReturnFlight.get(i).getId(), result.getReturnFlights().get(i).getId());
            assertEquals(listDirectReturnFlight.get(i).getCode(), result.getReturnFlights().get(i).getCode());
            assertEquals(listDirectReturnFlight.get(i).getDepartureDateTime(), result.getReturnFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectReturnFlight.get(i).getArrivalDateTime(), result.getReturnFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectReturnFlight.get(i).getFrom().getAirportCode(), result.getReturnFlights().get(i).getAirportFrom());
            assertEquals(listDirectReturnFlight.get(i).getTo().getAirportCode(), result.getReturnFlights().get(i).getAirportTo());
            assertEquals(listDirectReturnFlight.get(i).getFlightStatus(), result.getReturnFlights().get(i).getFlightStatus());
            assertEquals(listDirectReturnFlight.get(i).getAircraft().getId(), result.getReturnFlights().get(i).getAircraftId());
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
        directDepartureFlight1.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight1.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
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
        directDepartureFlight2.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 3, 0, 0));
        directDepartureFlight2.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 4, 0, 0));
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
        directReturnFlight1.setDepartureDateTime(LocalDateTime.of(2023, 4, 2, 5, 0, 0));
        directReturnFlight1.setArrivalDateTime(LocalDateTime.of(2023, 4, 2, 6, 0, 0));
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
        directReturnFlight2.setDepartureDateTime(LocalDateTime.of(2023, 4, 2, 10, 0, 0));
        directReturnFlight2.setArrivalDateTime(LocalDateTime.of(2023, 4, 2, 11, 0, 0));
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

        doReturn(new ArrayList<Flight>())
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                fromVnukovo.getId().intValue(),
                toKoltcovo.getId().intValue(),
                Date.valueOf(search.getReturnDate())
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

        assertEquals(listDirectDepartFlight.size(), 2);
        assertEquals(listDirectDepartFlight.size(), result.getDepartFlights().size());
        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
            assertEquals(listDirectDepartFlight.get(i).getId(), result.getDepartFlights().get(i).getId());
            assertEquals(listDirectDepartFlight.get(i).getCode(), result.getDepartFlights().get(i).getCode());
            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getDepartFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getDepartFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectDepartFlight.get(i).getFrom().getAirportCode(), result.getDepartFlights().get(i).getAirportFrom());
            assertEquals(listDirectDepartFlight.get(i).getTo().getAirportCode(), result.getDepartFlights().get(i).getAirportTo());
            assertEquals(listDirectDepartFlight.get(i).getFlightStatus(), result.getDepartFlights().get(i).getFlightStatus());
            assertEquals(listDirectDepartFlight.get(i).getAircraft().getId(), result.getDepartFlights().get(i).getAircraftId());
        }

        assertEquals(listDirectReturnFlight.size(), 2);
        assertEquals(listDirectReturnFlight.size(), result.getReturnFlights().size());
        for (int i = 0; i < listDirectReturnFlight.size(); i++) {
            assertEquals(listDirectReturnFlight.get(i).getId(), result.getReturnFlights().get(i).getId());
            assertEquals(listDirectReturnFlight.get(i).getCode(), result.getReturnFlights().get(i).getCode());
            assertEquals(listDirectReturnFlight.get(i).getDepartureDateTime(), result.getReturnFlights().get(i).getDepartureDateTime());
            assertEquals(listDirectReturnFlight.get(i).getArrivalDateTime(), result.getReturnFlights().get(i).getArrivalDateTime());
            assertEquals(listDirectReturnFlight.get(i).getFrom().getAirportCode(), result.getReturnFlights().get(i).getAirportFrom());
            assertEquals(listDirectReturnFlight.get(i).getTo().getAirportCode(), result.getReturnFlights().get(i).getAirportTo());
            assertEquals(listDirectReturnFlight.get(i).getFlightStatus(), result.getReturnFlights().get(i).getFlightStatus());
            assertEquals(listDirectReturnFlight.get(i).getAircraft().getId(), result.getReturnFlights().get(i).getAircraftId());
        }


    }


    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //


    @DisplayName("ХХХ search(), Positive test search one direct depart flight and one direct return flights")
    @Test
    public void shouldReturnSearchResultWhithOneDirectDepartFlightsAndOneDirectReturnFlight() {

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
        directDepartureFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
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
        directReturnFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 2, 5, 0, 0));
        directReturnFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 2, 6, 0, 0));
        directReturnFlight.setFlightStatus(FlightStatus.COMPLETED);
        directReturnFlight.setAircraft(aircraft2);
        directReturnFlight.setSeats(new ArrayList<FlightSeat>());

//        FlightDTO directDepartureFlightDTO = new FlightDTO();
//        directDepartureFlightDTO.setId(1L);
//        directDepartureFlightDTO.setCode("VKOSVX");
//        directDepartureFlightDTO.setAirportFrom(Airport.VKO);
//        directDepartureFlightDTO.setAirportTo(Airport.SVX);
//        directDepartureFlightDTO.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
//        directDepartureFlightDTO.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
//        directDepartureFlightDTO.setFlightStatus(FlightStatus.COMPLETED);
//        directDepartureFlightDTO.setAircraftId(1L);
//        directDepartureFlightDTO.setSeats(new ArrayList<FlightSeatDTO>());
//
//        FlightDTO directReturnFlightDTO = new FlightDTO();
//        directReturnFlightDTO.setId(2L);
//        directReturnFlightDTO.setCode("SVXVKO");
//        directReturnFlightDTO.setAirportFrom(Airport.SVX);
//        directReturnFlightDTO.setAirportTo(Airport.VKO);
//        directReturnFlightDTO.setDepartureDateTime(LocalDateTime.of(2023, 4, 2, 5, 0, 0));
//        directReturnFlightDTO.setArrivalDateTime(LocalDateTime.of(2023, 4, 2, 6, 0, 0));
//        directReturnFlightDTO.setFlightStatus(FlightStatus.COMPLETED);
//        directReturnFlightDTO.setAircraftId(2L);
//        directReturnFlightDTO.setSeats(new ArrayList<FlightSeatDTO>());
//
//        var directDepartureFlightDTOMap = doReturn(directDepartureFlightDTO).when(flightMapper).flightToFlightDTO(eq(directDepartureFlight), any());
//
//        var directReturnFlightDTOMap = doReturn(directReturnFlightDTO).when(flightMapper).flightToFlightDTO(eq(directReturnFlight), any());
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

//        var listDirectDepartureFlight = doReturn(List.of(directDepartureFlight))
//                .when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
//                        search.getTo(),
//                        search.getFrom(),
//                        Date.valueOf(search.getDepartureDate())
//                );
//
//
//        var listDirectReturnFlight = doReturn(List.of(directReturnFlight))
//                .when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
//                        search.getTo(),
//                        search.getFrom(),
//                        Date.valueOf(search.getReturnDate())
//                );

        doReturn(new ArrayList<Flight>()).when(flightService)
                .getListNonDirectFlightsByFromAndToAndDepartureDate(
                        fromVnukovo.getId().intValue(),
                        toKoltcovo.getId().intValue(),
                        Date.valueOf(search.getDepartureDate())
                );

        doReturn(new ArrayList<Flight>())
                .when(flightService).getListNonDirectFlightsByFromAndToAndDepartureDate(
                fromVnukovo.getId().intValue(),
                toKoltcovo.getId().intValue(),
                Date.valueOf(search.getReturnDate())
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

/*        result.getDepartFlights().forEach(a -> {
            System.out.println(a.getId());
            System.out.println(a.getCode());
            System.out.println(a.getDepartureDateTime());
            System.out.println(a.getArrivalDateTime());
            System.out.println(a.getAirportFrom());
            System.out.println(a.getAirportTo());
            System.out.println(a.getFlightStatus());
            System.out.println(a.getAircraftId());
        });

        System.out.println();
        System.out.println("-----------------------------------");
        System.out.println();

        result.getReturnFlights().forEach(a ->
        {
            System.out.println(a.getId());
            System.out.println(a.getCode());
            System.out.println(a.getDepartureDateTime());
            System.out.println(a.getArrivalDateTime());
            System.out.println(a.getAirportFrom());
            System.out.println(a.getAirportTo());
            System.out.println(a.getFlightStatus());
            System.out.println(a.getAircraftId());
        });*/

        assertEquals(listDirectDepartFlight.size(), 1);
        assertEquals(listDirectDepartFlight.size(), result.getDepartFlights().size());
        assertEquals(listDirectDepartFlight.get(0).getId(), result.getDepartFlights().get(0).getId());
        assertEquals(listDirectDepartFlight.get(0).getCode(), result.getDepartFlights().get(0).getCode());
        assertEquals(listDirectDepartFlight.get(0).getDepartureDateTime(), result.getDepartFlights().get(0).getDepartureDateTime());
        assertEquals(listDirectDepartFlight.get(0).getArrivalDateTime(), result.getDepartFlights().get(0).getArrivalDateTime());
        assertEquals(listDirectDepartFlight.get(0).getFrom().getAirportCode(), result.getDepartFlights().get(0).getAirportFrom());
        assertEquals(listDirectDepartFlight.get(0).getTo().getAirportCode(), result.getDepartFlights().get(0).getAirportTo());
        assertEquals(listDirectDepartFlight.get(0).getFlightStatus(), result.getDepartFlights().get(0).getFlightStatus());
        assertEquals(listDirectDepartFlight.get(0).getAircraft().getId(), result.getDepartFlights().get(0).getAircraftId());

        assertEquals(listDirectReturnFlight.size(), 1);
        assertEquals(listDirectReturnFlight.size(), result.getReturnFlights().size());
        assertEquals(listDirectReturnFlight.get(0).getId(), result.getReturnFlights().get(0).getId());
        assertEquals(listDirectReturnFlight.get(0).getCode(), result.getReturnFlights().get(0).getCode());
        assertEquals(listDirectReturnFlight.get(0).getDepartureDateTime(), result.getReturnFlights().get(0).getDepartureDateTime());
        assertEquals(listDirectReturnFlight.get(0).getArrivalDateTime(), result.getReturnFlights().get(0).getArrivalDateTime());
        assertEquals(listDirectReturnFlight.get(0).getFrom().getAirportCode(), result.getReturnFlights().get(0).getAirportFrom());
        assertEquals(listDirectReturnFlight.get(0).getTo().getAirportCode(), result.getReturnFlights().get(0).getAirportTo());
        assertEquals(listDirectReturnFlight.get(0).getFlightStatus(), result.getReturnFlights().get(0).getFlightStatus());
        assertEquals(listDirectReturnFlight.get(0).getAircraft().getId(), result.getReturnFlights().get(0).getAircraftId());


    }


    @DisplayName("1 search(), Positive test search 1 direct depart flight and 0 return flights")
    @Test
    public void nULLshouldReturnSearchResultWithOneDirectDepartFlightsWithoutReturnFlight() {

        Search search = new Search();
        search.setFrom(Airport.VKO);
        search.setTo(Airport.SVX);
        search.setDepartureDate(null);
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
        directDepartureFlight.setDepartureDateTime(LocalDateTime.of(2023, 4, 1, 1, 0, 0));
        directDepartureFlight.setArrivalDateTime(LocalDateTime.of(2023, 4, 1, 2, 0, 0));
        directDepartureFlight.setFlightStatus(FlightStatus.COMPLETED);
        directDepartureFlight.setAircraft(aircraft1);
        directDepartureFlight.setSeats(new ArrayList<FlightSeat>());

        var listDirectDepartFlight = List.of(directDepartureFlight);
//        var departureDate = Date.valueOf(search.getDepartureDate());
//        doReturn(listDirectDepartFlight).when(flightService).getListDirectFlightsByFromAndToAndDepartureDate(
//                any(Airport.class), any(Airport.class), eq(departureDate)
//        );

//        doReturn(new ArrayList<Flight>()).when(flightService)
//                .getListNonDirectFlightsByFromAndToAndDepartureDate(
//                        fromVnukovo.getId().intValue(),
//                        toKoltcovo.getId().intValue(),
//                        Date.valueOf(search.getDepartureDate())
//                );

//        doReturn(fromVnukovo).when(destinationService).getDestinationByAirportCode(search.getFrom());
//
//        doReturn(toKoltcovo).when(destinationService).getDestinationByAirportCode(search.getTo());
//
//        doReturn(2).when(flightSeatService).getNumberOfFreeSeatOnFlight(any(Flight.class));

        SearchResult result = searchService.search(
                search.getFrom(),
                search.getTo(),
                search.getDepartureDate(),
                search.getReturnDate(),
                search.getNumberOfPassengers()
        );


//        assertEquals(listDirectDepartFlight.size(), 1);
        assertEquals(0, result.getDepartFlights().size());
//        for (int i = 0; i < listDirectDepartFlight.size(); i++) {
//            assertEquals(listDirectDepartFlight.get(i).getId(), result.getDepartFlights().get(i).getId());
//            assertEquals(listDirectDepartFlight.get(i).getCode(), result.getDepartFlights().get(i).getCode());
//            assertEquals(listDirectDepartFlight.get(i).getDepartureDateTime(), result.getDepartFlights().get(i).getDepartureDateTime());
//            assertEquals(listDirectDepartFlight.get(i).getArrivalDateTime(), result.getDepartFlights().get(i).getArrivalDateTime());
//            assertEquals(listDirectDepartFlight.get(i).getFrom().getAirportCode(), result.getDepartFlights().get(i).getAirportFrom());
//            assertEquals(listDirectDepartFlight.get(i).getTo().getAirportCode(), result.getDepartFlights().get(i).getAirportTo());
//            assertEquals(listDirectDepartFlight.get(i).getFlightStatus(), result.getDepartFlights().get(i).getFlightStatus());
//            assertEquals(listDirectDepartFlight.get(i).getAircraft().getId(), result.getDepartFlights().get(i).getAircraftId());
//        }


    }

}
