package app.mappers;

import app.dto.FlightDTO;
import app.entities.*;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class FlightMapperTest {

    FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);
    @Mock
    private AircraftService aircraftServiceMock = Mockito.mock(AircraftService.class);
    @Mock
    private DestinationService destinationServiceMock = Mockito.mock(DestinationService.class);
    @Mock
    private TicketService ticketServiceMock = Mockito.mock(TicketService.class);
    @Mock
    private BookingService bookingServiceMock = Mockito.mock(BookingService.class);
    @Mock
    private FlightSeatService flightSeatServiceMock = Mockito.mock(FlightSeatService.class);

    @Test
    public void shouldConvertFlightToFlightDTOEntity() throws Exception {
        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);

        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatList.add(flightSeat1);
        flightSeatList.add(flightSeat2);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2001L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2002L);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        Booking booking1 = new Booking();
        booking1.setId(3001L);

        Booking booking2 = new Booking();
        booking2.setId(3002L);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking2);

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        LocalDateTime departureDateTime = LocalDateTime.MIN;

        LocalDateTime arrivalDateTime = LocalDateTime.MAX;

        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setSeats(flightSeatList);
        flight.setTicket(ticketList);
        flight.setBooking(bookingList);
        flight.setCode("qwerty123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        flight.setDepartureDateTime(departureDateTime);
        flight.setArrivalDateTime(arrivalDateTime);
        flight.setAircraft(aircraft);
        flight.setFlightStatus(FlightStatus.ON_TIME);

        FlightDTO flightDTO = flightMapper.flightToFlightDTO(flight);

        Assertions.assertNotNull(flightDTO);
        Assertions.assertEquals(flightDTO.getId(), flight.getId());
        Assertions.assertEquals(flightDTO.getCode(), flight.getCode());
        Assertions.assertEquals(flightDTO.getAirportFrom(), flight.getFrom().getAirportCode());
        Assertions.assertEquals(flightDTO.getAirportTo(), flight.getTo().getAirportCode());
        Assertions.assertEquals(flightDTO.getArrivalDateTime(), flight.getArrivalDateTime());
        Assertions.assertEquals(flightDTO.getDepartureDateTime(), flight.getDepartureDateTime());
        Assertions.assertEquals(flightDTO.getAircraftId(), flight.getAircraft().getId());
        Assertions.assertEquals(flightDTO.getFlightStatus(), flight.getFlightStatus());
    }

    @Test
    public void shouldConvertFlightDTOtoFlightEntity() throws Exception {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(1001L);
        flightDTO.setAirportTo(Airport.AAQ);
        flightDTO.setAirportFrom(Airport.ABA);
        flightDTO.setArrivalDateTime(LocalDateTime.MAX);
        flightDTO.setDepartureDateTime(LocalDateTime.MIN);
        flightDTO.setAircraftId(6001L);
        flightDTO.setFlightStatus(FlightStatus.CANCELED);

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);

        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatList.add(flightSeat1);
        flightSeatList.add(flightSeat2);

        when(flightSeatServiceMock.findByFlightId(flightDTO.getId())).thenReturn(flightSeatList);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2001L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2002L);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        when(ticketServiceMock.findByFlightId(flightDTO.getId())).thenReturn(ticketList);

        Booking booking1 = new Booking();
        booking1.setId(3001L);

        Booking booking2 = new Booking();
        booking2.setId(3002L);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking2);

        when(bookingServiceMock.findByFlightId(flightDTO.getId())).thenReturn(bookingList);

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        when(destinationServiceMock.getDestinationByAirportCode(flightDTO.getAirportFrom())).thenReturn(destinationFrom);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        when(destinationServiceMock.getDestinationByAirportCode(flightDTO.getAirportTo())).thenReturn(destinationTo);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        when(aircraftServiceMock.getAircraftById(flightDTO.getAircraftId())).thenReturn(aircraft);

        Flight flight = flightMapper.flightDTOtoFlight(flightDTO, aircraftServiceMock, destinationServiceMock,
                ticketServiceMock, bookingServiceMock, flightSeatServiceMock);

        Assertions.assertNotNull(flight);
        Assertions.assertEquals(flight.getId(), flightDTO.getId());
        Assertions.assertEquals(flight.getSeats(), flightSeatList);
        Assertions.assertEquals(flight.getTicket(), ticketList);
        Assertions.assertEquals(flight.getBooking(), bookingList);
        Assertions.assertEquals(flight.getCode(), flightDTO.getCode());
        Assertions.assertEquals(flight.getFrom().getAirportCode(), flightDTO.getAirportFrom());
        Assertions.assertEquals(flight.getTo().getAirportCode(), flightDTO.getAirportTo());
        Assertions.assertEquals(flight.getArrivalDateTime(), flightDTO.getArrivalDateTime());
        Assertions.assertEquals(flight.getDepartureDateTime(), flightDTO.getDepartureDateTime());
        Assertions.assertEquals(flight.getAircraft().getId(), flightDTO.getAircraftId());
        Assertions.assertEquals(flight.getFlightStatus(), flightDTO.getFlightStatus());
    }
}
