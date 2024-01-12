package app.mappers;

import app.dto.FlightDto;
import app.entities.*;
import app.enums.Airport;
import app.enums.FlightStatus;
import app.services.*;
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
    void shouldConvertFlightToFlightDtoEntity() throws Exception {
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
        FlightDto flightDto = flightMapper.flightToFlightDto(flight, flightServiceMock);

        Assertions.assertNotNull(flightDto);
        Assertions.assertEquals(flightDto.getId(), flight.getId());
        Assertions.assertEquals(flightDto.getCode(), flight.getCode());
        Assertions.assertEquals(flightDto.getAirportFrom(), flight.getFrom().getAirportCode());
        Assertions.assertEquals(flightDto.getAirportTo(), flight.getTo().getAirportCode());
        Assertions.assertEquals(flightDto.getArrivalDateTime(), flight.getArrivalDateTime());
        Assertions.assertEquals(flightDto.getDepartureDateTime(), flight.getDepartureDateTime());
        Assertions.assertEquals(flightDto.getAircraftId(), flight.getAircraft().getId());
        Assertions.assertEquals(flightDto.getFlightStatus(), flight.getFlightStatus());
    }

    @Test
    void shouldConvertFlightDtotoFlightEntity() throws Exception {
        FlightDto flightDto = new FlightDto();
        flightDto.setId(1001L);
        flightDto.setAirportTo(Airport.AAQ);
        flightDto.setAirportFrom(Airport.ABA);
        flightDto.setArrivalDateTime(LocalDateTime.MAX);
        flightDto.setDepartureDateTime(LocalDateTime.MIN);
        flightDto.setAircraftId(6001L);
        flightDto.setFlightStatus(FlightStatus.CANCELED);

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);

        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatList.add(flightSeat1);
        flightSeatList.add(flightSeat2);

        when(flightSeatServiceMock.findByFlightId(flightDto.getId())).thenReturn(flightSeatList);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2001L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2002L);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        when(ticketServiceMock.findByFlightId(flightDto.getId())).thenReturn(ticketList);

        Booking booking1 = new Booking();
        booking1.setId(3001L);

        Booking booking2 = new Booking();
        booking2.setId(3002L);

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        when(destinationServiceMock.getDestinationByAirportCode(flightDto.getAirportFrom())).thenReturn(destinationFrom);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        when(destinationServiceMock.getDestinationByAirportCode(flightDto.getAirportTo())).thenReturn(destinationTo);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        when(aircraftServiceMock.getAircraftById(flightDto.getAircraftId())).thenReturn(aircraft);

        Flight flight = flightMapper.flightDtotoFlight(flightDto, aircraftServiceMock, destinationServiceMock,
                ticketServiceMock, flightSeatServiceMock);

        Assertions.assertNotNull(flight);
        Assertions.assertEquals(flight.getId(), flightDto.getId());
        Assertions.assertEquals(flight.getSeats(), flightSeatList);
        Assertions.assertEquals(flight.getTicket(), ticketList);
        Assertions.assertEquals(flight.getCode(), flightDto.getCode());
        Assertions.assertEquals(flight.getFrom().getAirportCode(), flightDto.getAirportFrom());
        Assertions.assertEquals(flight.getTo().getAirportCode(), flightDto.getAirportTo());
        Assertions.assertEquals(flight.getArrivalDateTime(), flightDto.getArrivalDateTime());
        Assertions.assertEquals(flight.getDepartureDateTime(), flightDto.getDepartureDateTime());
        Assertions.assertEquals(flight.getAircraft().getId(), flightDto.getAircraftId());
        Assertions.assertEquals(flight.getFlightStatus(), flightDto.getFlightStatus());
    }

    @Test
    void shouldConvertFlightListToFlightDtoList() throws Exception {
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

        List<FlightDto> flightDtoList = flightMapper.convertFlightListToFlighDtotList(flightList, flightServiceMock);

        Assertions.assertEquals(flightList.size(), flightDtoList.size());
        Assertions.assertEquals(flightDtoList.get(0).getId(), flightList.get(0).getId());
        Assertions.assertEquals(flightDtoList.get(0).getCode(), flightList.get(0).getCode());
        Assertions.assertEquals(flightDtoList.get(0).getAirportFrom(), flightList.get(0).getFrom().getAirportCode());
        Assertions.assertEquals(flightDtoList.get(0).getAirportTo(), flightList.get(0).getTo().getAirportCode());
        Assertions.assertEquals(flightDtoList.get(0).getArrivalDateTime(), flightList.get(0).getArrivalDateTime());
        Assertions.assertEquals(flightDtoList.get(0).getDepartureDateTime(), flightList.get(0).getDepartureDateTime());
        Assertions.assertEquals(flightDtoList.get(0).getAircraftId(), flightList.get(0).getAircraft().getId());
        Assertions.assertEquals(flightDtoList.get(0).getFlightStatus(), flightList.get(0).getFlightStatus());
    }

    @Test
    void shouldConvertFlightDtoListToFlightEntityList() throws Exception {
        List<FlightDto> flightDtoList = new ArrayList<>();
        FlightDto flightDto = new FlightDto();
        flightDto.setId(1001L);
        flightDto.setAirportTo(Airport.AAQ);
        flightDto.setAirportFrom(Airport.ABA);
        flightDto.setArrivalDateTime(LocalDateTime.MAX);
        flightDto.setDepartureDateTime(LocalDateTime.MIN);
        flightDto.setAircraftId(6001L);
        flightDto.setFlightStatus(FlightStatus.CANCELED);

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(1001L);

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(1002L);

        List<FlightSeat> flightSeatList = new ArrayList<>();
        flightSeatList.add(flightSeat1);
        flightSeatList.add(flightSeat2);

        when(flightSeatServiceMock.findByFlightId(flightDto.getId())).thenReturn(flightSeatList);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2001L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2002L);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        when(ticketServiceMock.findByFlightId(flightDto.getId())).thenReturn(ticketList);

        Booking booking1 = new Booking();
        booking1.setId(3001L);

        Booking booking2 = new Booking();
        booking2.setId(3002L);

        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);

        when(destinationServiceMock.getDestinationByAirportCode(flightDto.getAirportFrom())).thenReturn(destinationFrom);

        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        when(destinationServiceMock.getDestinationByAirportCode(flightDto.getAirportTo())).thenReturn(destinationTo);

        Aircraft aircraft = new Aircraft();
        aircraft.setId(6001L);

        when(aircraftServiceMock.getAircraftById(flightDto.getAircraftId())).thenReturn(aircraft);

        flightDtoList.add(flightDto);

        List<Flight> flightList = flightMapper.convertFlightDtoListToFlightList(flightDtoList, aircraftServiceMock, destinationServiceMock,
                ticketServiceMock, flightSeatServiceMock);

        Assertions.assertEquals(flightList.size(), flightDtoList.size());
        Assertions.assertEquals(flightList.get(0).getId(), flightDtoList.get(0).getId());
        Assertions.assertEquals(flightList.get(0).getSeats(), flightSeatList);
        Assertions.assertEquals(flightList.get(0).getTicket(), ticketList);
        Assertions.assertEquals(flightList.get(0).getCode(), flightDtoList.get(0).getCode());
        Assertions.assertEquals(flightList.get(0).getFrom().getAirportCode(), flightDtoList.get(0).getAirportFrom());
        Assertions.assertEquals(flightList.get(0).getTo().getAirportCode(), flightDtoList.get(0).getAirportTo());
        Assertions.assertEquals(flightList.get(0).getArrivalDateTime(), flightDtoList.get(0).getArrivalDateTime());
        Assertions.assertEquals(flightList.get(0).getDepartureDateTime(), flightDtoList.get(0).getDepartureDateTime());
        Assertions.assertEquals(flightList.get(0).getAircraft().getId(), flightDtoList.get(0).getAircraftId());
        Assertions.assertEquals(flightList.get(0).getFlightStatus(), flightDtoList.get(0).getFlightStatus());
    }
}
