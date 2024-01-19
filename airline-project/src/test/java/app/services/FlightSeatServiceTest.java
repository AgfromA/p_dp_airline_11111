package app.services;

import app.entities.Category;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.Seat;
import app.enums.Airport;
import app.enums.CategoryType;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testGenerateFareForFlightSeat() {

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