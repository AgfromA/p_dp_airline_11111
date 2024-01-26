package app.services;

import app.entities.Destination;
import app.entities.Flight;
import app.enums.Airport;
import org.junit.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    private final FlightService flightService = new FlightService(null, null, null, null);

    @Test
    public void testGetDistance() {

        var from = new Destination();
        from.setAirportCode(Airport.OMS);
        var to = new Destination();
        to.setAirportCode(Airport.SVX);

        var flight = new Flight();
        flight.setFrom(from);
        flight.setTo(to);

        assertEquals(807L, flightService.getDistance(flight));
    }

    @Test
    public void testParseLatitude() {
        assertEquals(54.957875, flightService.parseLatitude(Airport.OMS), 0.000001);
    }

    @Test
    public void testParseLongitude() {
        assertEquals(73.316683, flightService.parseLongitude(Airport.OMS), 0.000001);
    }
}