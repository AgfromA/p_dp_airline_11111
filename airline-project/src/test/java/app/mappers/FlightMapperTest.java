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
import java.util.Optional;

import static app.enums.CategoryType.BUSINESS;
import static org.mockito.Mockito.*;

class FlightMapperTest {

    FlightMapper flightMapper = Mappers.getMapper(FlightMapper.class);
    @Mock
    private AircraftService aircraftServiceMock = Mockito.mock(AircraftService.class);
    @Mock
    private DestinationService destinationServiceMock = Mockito.mock(DestinationService.class);
    @Mock
    private TicketService ticketServiceMock = Mockito.mock(TicketService.class);
    @Mock
    private FlightSeatService flightSeatServiceMock = Mockito.mock(FlightSeatService.class);
    @Mock
    private FlightService flightServiceMock = Mockito.mock(FlightService.class);
    @Mock
    private SeatService seatServiceMock = Mockito.mock(SeatService.class);

    @Test
    void shouldConvertFlightToFlightDTOEntity() throws Exception {
        Category category = new Category();
        category.setCategoryType(BUSINESS);
        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setCategory(category);
        seat1.setAircraft(aircraft);
        Seat seat2 = new Seat();
        seat2.setId(2);
        seat2.setCategory(category);
        seat2.setAircraft(aircraft);

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);
        flightSeat1.setSeat(seat1);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);
        flightSeat2.setSeat(seat2);

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

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        LocalDateTime departureDateTime = LocalDateTime.MIN;

        LocalDateTime arrivalDateTime = LocalDateTime.MAX;

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setSeats(flightSeatList);
        flight.setTicket(ticketList);
        flight.setCode("qwerty123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        flight.setDepartureDateTime(departureDateTime);
        flight.setArrivalDateTime(arrivalDateTime);
        flight.setAircraft(aircraft);
        flight.setFlightStatus(FlightStatus.ON_TIME);
        flightSeat1.setFlight(flight);
        flightSeat2.setFlight(flight);

        when(flightServiceMock.getFlightById(1L)).thenReturn(Optional.of(flight));
        when(seatServiceMock.getSeatById(anyLong())).thenReturn(flightSeat1.getSeat());
        FlightDTO flightDTO = flightMapper.flightToFlightDTO(flight, flightServiceMock);

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
    void shouldConvertFlightDTOtoFlightEntity() throws Exception {
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
                ticketServiceMock, flightSeatServiceMock);

        Assertions.assertNotNull(flight);
        Assertions.assertEquals(flight.getId(), flightDTO.getId());
        Assertions.assertEquals(flight.getSeats(), flightSeatList);
        Assertions.assertEquals(flight.getTicket(), ticketList);
        Assertions.assertEquals(flight.getCode(), flightDTO.getCode());
        Assertions.assertEquals(flight.getFrom().getAirportCode(), flightDTO.getAirportFrom());
        Assertions.assertEquals(flight.getTo().getAirportCode(), flightDTO.getAirportTo());
        Assertions.assertEquals(flight.getArrivalDateTime(), flightDTO.getArrivalDateTime());
        Assertions.assertEquals(flight.getDepartureDateTime(), flightDTO.getDepartureDateTime());
        Assertions.assertEquals(flight.getAircraft().getId(), flightDTO.getAircraftId());
        Assertions.assertEquals(flight.getFlightStatus(), flightDTO.getFlightStatus());
    }

    @Test
    void shouldConvertFlightListToFlightDTOList() throws Exception {
        List<Flight> flightList = new ArrayList<>();
        Category category = new Category();
        category.setCategoryType(BUSINESS);
        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setCategory(category);
        seat1.setAircraft(aircraft);
        Seat seat2 = new Seat();
        seat2.setId(2);
        seat2.setCategory(category);
        seat2.setAircraft(aircraft);

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);
        flightSeat1.setSeat(seat1);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);
        flightSeat2.setSeat(seat2);

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

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        LocalDateTime departureDateTime = LocalDateTime.MIN;

        LocalDateTime arrivalDateTime = LocalDateTime.MAX;

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setSeats(flightSeatList);
        flight.setTicket(ticketList);
        flight.setCode("qwerty123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        flight.setDepartureDateTime(departureDateTime);
        flight.setArrivalDateTime(arrivalDateTime);
        flight.setAircraft(aircraft);
        flight.setFlightStatus(FlightStatus.ON_TIME);
        flightSeat1.setFlight(flight);
        flightSeat2.setFlight(flight);

        when(flightServiceMock.getFlightById(1L)).thenReturn(Optional.of(flight));
        when(seatServiceMock.getSeatById(anyLong())).thenReturn(flightSeat1.getSeat());

        flightList.add(flight);

        List<FlightDTO> flightDTOList = flightMapper.convertFlightListToFlighDTOtList(flightList, flightServiceMock);

        Assertions.assertEquals(flightList.size(), flightDTOList.size());
        Assertions.assertEquals(flightDTOList.get(0).getId(), flightList.get(0).getId());
        Assertions.assertEquals(flightDTOList.get(0).getCode(), flightList.get(0).getCode());
        Assertions.assertEquals(flightDTOList.get(0).getAirportFrom(), flightList.get(0).getFrom().getAirportCode());
        Assertions.assertEquals(flightDTOList.get(0).getAirportTo(), flightList.get(0).getTo().getAirportCode());
        Assertions.assertEquals(flightDTOList.get(0).getArrivalDateTime(), flightList.get(0).getArrivalDateTime());
        Assertions.assertEquals(flightDTOList.get(0).getDepartureDateTime(), flightList.get(0).getDepartureDateTime());
        Assertions.assertEquals(flightDTOList.get(0).getAircraftId(), flightList.get(0).getAircraft().getId());
        Assertions.assertEquals(flightDTOList.get(0).getFlightStatus(), flightList.get(0).getFlightStatus());
    }

    @Test
    void shouldConvertFlightDTOListToFlightEntityList() throws Exception {
        List<FlightDTO> flightDTOList = new ArrayList<>();
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

        flightDTOList.add(flightDTO);

        List<Flight> flightList = flightMapper.convertFlightDTOListToFlightList(flightDTOList, aircraftServiceMock, destinationServiceMock,
                ticketServiceMock, flightSeatServiceMock);

        Assertions.assertEquals(flightList.size(), flightDTOList.size());
        Assertions.assertEquals(flightList.get(0).getId(), flightDTOList.get(0).getId());
        Assertions.assertEquals(flightList.get(0).getSeats(), flightSeatList);
        Assertions.assertEquals(flightList.get(0).getTicket(), ticketList);
        Assertions.assertEquals(flightList.get(0).getCode(), flightDTOList.get(0).getCode());
        Assertions.assertEquals(flightList.get(0).getFrom().getAirportCode(), flightDTOList.get(0).getAirportFrom());
        Assertions.assertEquals(flightList.get(0).getTo().getAirportCode(), flightDTOList.get(0).getAirportTo());
        Assertions.assertEquals(flightList.get(0).getArrivalDateTime(), flightDTOList.get(0).getArrivalDateTime());
        Assertions.assertEquals(flightList.get(0).getDepartureDateTime(), flightDTOList.get(0).getDepartureDateTime());
        Assertions.assertEquals(flightList.get(0).getAircraft().getId(), flightDTOList.get(0).getAircraftId());
        Assertions.assertEquals(flightList.get(0).getFlightStatus(), flightDTOList.get(0).getFlightStatus());
    }
}
