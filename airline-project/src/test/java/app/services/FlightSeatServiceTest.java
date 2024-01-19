package app.services;

import app.entities.*;
import app.enums.Airport;
import app.enums.CategoryType;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightSeatServiceTest {

    @Mock
    FlightSeatRepository flightSeatRepository;

    @Mock
    FlightRepository flightRepository;

    @Mock
    FlightService flightService;

    @InjectMocks
    FlightSeatService flightSeatService;

    @Test
    void addFlightSeatsByFlightNumber() {
        when(flightService.getDistance(any(Flight.class))).thenReturn(807L);

        var flightNumber = "Code:Fl-1";

        var aircraft = new Aircraft();
        aircraft.setId(Long.valueOf(1));
        aircraft.setAircraftNumber("Number:A-1");
        aircraft.setModel("ModelAir");
        aircraft.setFlightRange(500);
        aircraft.setModelYear(2008);

        var category = new Category();
        category.setCategoryType(CategoryType.BUSINESS);

        //Список мест самолёта с номерами от 8 до 17
        Set<Seat> seatSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            var seat = new Seat();
            seat.setSeatNumber(Integer.valueOf(i + 8).toString());
            seat.setIsNearEmergencyExit(true);
            seat.setIsLockedBack(true);
            seat.setCategory(category);
            seat.setAircraft(aircraft);
            seatSet.add(seat);
        }

        aircraft.setSeatSet(seatSet);

        var flight = new Flight();
        flight.setCode(flightNumber);
        flight.setAircraft(aircraft);

        //Список уже  существующих мест для продажи с номерами от 1 до 10
        //№8 - такой же номер как в самолёте, но не относится к нему
        //№9 и №10 - уже добавленные места нашего самолёта для этого Flight-а
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            var flightSeat = new FlightSeat();
            Seat seat = new Seat();
            seat.setSeatNumber(Integer.valueOf(i + 1).toString());

            if ((i + 1) >= 9) {
                seat.setAircraft(aircraft);
                flightSeat.setFlight(flight);
            }
            flightSeat.setSeat(seat);
            flightSeatSet.add(flightSeat);
        }

        Mockito.doReturn(flight)
                .when(flightRepository)
                .getByCode(flightNumber);

        Mockito.doReturn(flightSeatSet)
                .when(flightSeatRepository)
                .findAll();

        var result = flightSeatService.addFlightSeatsByFlightNumber(flightNumber);

        Mockito.verify(flightSeatRepository, Mockito.times(1)).saveAll(any());

        assertEquals(8, result.size());

        result.forEach(flightSeat -> assertEquals(flight, flightSeat.getFlight()));

        Set<Seat> seatsInResult = new HashSet<>();
        result.forEach(flightSeat -> seatsInResult.add(flightSeat.getSeat()));
        seatSet.removeIf(seat -> seat.getSeatNumber().equals(Integer.valueOf(9).toString())
                || seat.getSeatNumber().equals(Integer.valueOf(10).toString()));
        seatSet.addAll(seatsInResult);
        assertEquals(8, seatSet.size());

    }

    @Test
    public void testGenerateFareForFlightSeat() {

        var from = new Destination();
        from.setAirportCode(Airport.SVX);
        var to = new Destination();
        to.setAirportCode(Airport.VKO);

        var flight = new Flight();
        flight.setFrom(from);
        flight.setTo(to);

        var category = new Category();
        category.setCategoryType(CategoryType.BUSINESS);

        var seat = new Seat();
        seat.setIsNearEmergencyExit(true);
        seat.setIsLockedBack(true);
        seat.setCategory(category);

        when(flightService.getDistance(any(Flight.class))).thenReturn(1459L);

        assertEquals(11920,  flightSeatService.generateFareForFlightSeat(seat, flight));
    }
}