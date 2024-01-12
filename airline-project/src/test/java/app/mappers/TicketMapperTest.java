package app.mappers;

import app.dto.TicketDto;
import app.entities.*;
import app.enums.Airport;
import app.services.FlightSeatService;
import app.services.FlightService;
import app.services.PassengerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class TicketMapperTest {
    TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);
    @Mock
    private PassengerService passengerServiceMock = Mockito.mock(PassengerService.class);
    @Mock
    private FlightService flightServiceMock = Mockito.mock(FlightService.class);

    @Mock
    private FlightSeatService flightSeatServiceMock = Mockito.mock(FlightSeatService.class);

    @Test
    @DisplayName("Должен корректно конвертировать сущность в ДТО")
    void shouldConvertTicketEntityToTicketDTO() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        Flight flight = new Flight();
        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);
        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);
        flight.setId(2001L);
        flight.setCode("TEST123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        when(flightServiceMock.getFlightById(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        when(flightSeatServiceMock.getFlightSeatById(3001L)).thenReturn(Optional.of(flightSeat));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("OX-2010");
        ticket.setPassenger(passenger);
        ticket.setFlight(flight);
        ticket.setFlightSeat(flightSeat);

        TicketDto ticketDTO = ticketMapper.toDto(ticket);

        Assertions.assertNotNull(ticketDTO);
        Assertions.assertEquals(ticket.getId(), ticketDTO.getId());
        Assertions.assertEquals(ticket.getTicketNumber(), ticketDTO.getTicketNumber());
        Assertions.assertEquals(ticket.getPassenger().getId(), ticketDTO.getPassengerId());
        Assertions.assertEquals(ticket.getPassenger().getFirstName(), ticketDTO.getFirstName());
        Assertions.assertEquals(ticket.getPassenger().getLastName(), ticketDTO.getLastName());

        Assertions.assertEquals(ticket.getFlight().getId(), ticketDTO.getFlightId());
        Assertions.assertEquals(ticket.getFlight().getCode(), ticketDTO.getCode());
        Assertions.assertEquals(ticket.getFlight().getFrom().getAirportCode(), ticketDTO.getFrom());
        Assertions.assertEquals(ticket.getFlight().getTo().getAirportCode(), ticketDTO.getTo());
        Assertions.assertEquals(ticket.getFlight().getDepartureDateTime(), ticketDTO.getDepartureDateTime());
        Assertions.assertEquals(ticket.getFlight().getArrivalDateTime(), ticketDTO.getArrivalDateTime());

        Assertions.assertEquals(ticket.getFlightSeat().getId(), ticketDTO.getFlightSeatId());
        Assertions.assertEquals(ticket.getFlightSeat().getSeat().getSeatNumber(), ticketDTO.getSeatNumber());
    }

    @Test
    @DisplayName("Должен корректно конвертировать ДТО в сущность")
    void shouldConvertTicketDTOToTicketEntity() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        Flight flight = new Flight();
        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);
        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        flight.setId(2001L);
        flight.setCode("TEST123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        when(flightServiceMock.getFlightById(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        when(flightSeatServiceMock.getFlightSeatById(3001L)).thenReturn(Optional.of(flightSeat));

        TicketDto ticketDTO = new TicketDto();
        ticketDTO.setId(1L);
        ticketDTO.setTicketNumber("OX-2010");
        ticketDTO.setPassengerId(1001L);
        ticketDTO.setFirstName("Test");
        ticketDTO.setLastName("Testing");

        ticketDTO.setFlightId(2001L);
        ticketDTO.setCode("TEST123");
        ticketDTO.setFrom(Airport.ABA);
        ticketDTO.setTo(Airport.AAQ);
        ticketDTO.setDepartureDateTime(null);
        ticketDTO.setArrivalDateTime(null);

        ticketDTO.setFlightSeatId(3001L);
        ticketDTO.setSeatNumber("42L");

        Ticket ticket = ticketMapper.toEntity(ticketDTO, passengerServiceMock, flightServiceMock, flightSeatServiceMock);

        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(ticketDTO.getId(), ticket.getId());
        Assertions.assertEquals(ticketDTO.getTicketNumber(), ticket.getTicketNumber());
        Assertions.assertEquals(ticketDTO.getPassengerId(), ticket.getPassenger().getId());
        Assertions.assertEquals(ticketDTO.getFirstName(), ticket.getPassenger().getFirstName());
        Assertions.assertEquals(ticketDTO.getLastName(), ticket.getPassenger().getLastName());

        Assertions.assertEquals(ticketDTO.getFlightId(), ticket.getFlight().getId());
        Assertions.assertEquals(ticketDTO.getCode(), ticket.getFlight().getCode());
        Assertions.assertEquals(ticketDTO.getFrom(), ticket.getFlight().getFrom().getAirportCode());
        Assertions.assertEquals(ticketDTO.getTo(), ticket.getFlight().getTo().getAirportCode());
        Assertions.assertEquals(ticket.getFlight().getDepartureDateTime(), ticketDTO.getDepartureDateTime());
        Assertions.assertEquals(ticket.getFlight().getArrivalDateTime(), ticketDTO.getArrivalDateTime());

        Assertions.assertEquals(ticketDTO.getFlightSeatId(), ticket.getFlightSeat().getId());
        Assertions.assertEquals(ticketDTO.getSeatNumber(), ticket.getFlightSeat().getSeat().getSeatNumber());

    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию сущностей в ДТО")
    void shouldConvertTicketEntityListToTicketDTOList() throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        Flight flight = new Flight();
        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);
        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);
        flight.setId(2001L);
        flight.setCode("TEST123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        when(flightServiceMock.getFlightById(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        when(flightSeatServiceMock.getFlightSeatById(3001L)).thenReturn(Optional.of(flightSeat));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("OX-2010");
        ticket.setPassenger(passenger);
        ticket.setFlight(flight);
        ticket.setFlightSeat(flightSeat);

        ticketList.add(ticket);

        List<TicketDto> ticketDtoList = ticketMapper.toDtoList(ticketList);

        Assertions.assertEquals(ticketList.size(), ticketDtoList.size());
        Assertions.assertEquals(ticketList.get(0).getId(), ticketDtoList.get(0).getId());
        Assertions.assertEquals(ticketList.get(0).getTicketNumber(), ticketDtoList.get(0).getTicketNumber());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getId(), ticketDtoList.get(0).getPassengerId());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getFirstName(), ticketDtoList.get(0).getFirstName());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getLastName(), ticketDtoList.get(0).getLastName());

        Assertions.assertEquals(ticketList.get(0).getFlight().getId(), ticketDtoList.get(0).getFlightId());
        Assertions.assertEquals(ticketList.get(0).getFlight().getCode(), ticketDtoList.get(0).getCode());
        Assertions.assertEquals(ticketList.get(0).getFlight().getFrom().getAirportCode(), ticketDtoList.get(0).getFrom());
        Assertions.assertEquals(ticketList.get(0).getFlight().getTo().getAirportCode(), ticketDtoList.get(0).getTo());
        Assertions.assertEquals(ticketList.get(0).getFlight().getDepartureDateTime(), ticketDtoList.get(0).getDepartureDateTime());
        Assertions.assertEquals(ticketList.get(0).getFlight().getArrivalDateTime(), ticketDtoList.get(0).getArrivalDateTime());

        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getId(), ticketDtoList.get(0).getFlightSeatId());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getSeat().getSeatNumber(), ticketDtoList.get(0).getSeatNumber());
    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию ДТО в сущности")
    void shouldConvertTicketDTOLisToTicketEntityList() throws Exception {
        List<TicketDto> ticketDtoList = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        Flight flight = new Flight();
        Destination destinationFrom = new Destination();
        destinationFrom.setId(4001L);
        destinationFrom.setAirportCode(Airport.ABA);
        Destination destinationTo = new Destination();
        destinationTo.setId(5001L);
        destinationTo.setAirportCode(Airport.AAQ);

        flight.setId(2001L);
        flight.setCode("TEST123");
        flight.setFrom(destinationFrom);
        flight.setTo(destinationTo);
        when(flightServiceMock.getFlightById(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        when(flightSeatServiceMock.getFlightSeatById(3001L)).thenReturn(Optional.of(flightSeat));

        TicketDto ticketDTO = new TicketDto();
        ticketDTO.setId(1L);
        ticketDTO.setTicketNumber("OX-2010");
        ticketDTO.setPassengerId(1001L);
        ticketDTO.setFirstName("Test");
        ticketDTO.setLastName("Testing");

        ticketDTO.setFlightId(2001L);
        ticketDTO.setCode("TEST123");
        ticketDTO.setFrom(Airport.ABA);
        ticketDTO.setTo(Airport.AAQ);
        ticketDTO.setDepartureDateTime(null);
        ticketDTO.setArrivalDateTime(null);

        ticketDTO.setFlightSeatId(3001L);
        ticketDTO.setSeatNumber("42L");

        ticketDtoList.add(ticketDTO);

        List<Ticket> ticketList = ticketMapper.toEntityList(ticketDtoList, passengerServiceMock,
                flightServiceMock, flightSeatServiceMock);

        Assertions.assertEquals(ticketDtoList.size(), ticketList.size());
        Assertions.assertEquals(ticketDtoList.get(0).getId(), ticketList.get(0).getId());
        Assertions.assertEquals(ticketDtoList.get(0).getTicketNumber(), ticketList.get(0).getTicketNumber());
        Assertions.assertEquals(ticketDtoList.get(0).getPassengerId(), ticketList.get(0).getPassenger().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getFirstName(), ticketList.get(0).getPassenger().getFirstName());
        Assertions.assertEquals(ticketDtoList.get(0).getLastName(), ticketList.get(0).getPassenger().getLastName());

        Assertions.assertEquals(ticketDtoList.get(0).getFlightId(), ticketList.get(0).getFlight().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getCode(), ticketList.get(0).getFlight().getCode());
        Assertions.assertEquals(ticketDtoList.get(0).getFrom(), ticketList.get(0).getFlight().getFrom().getAirportCode());
        Assertions.assertEquals(ticketDtoList.get(0).getTo(), ticketList.get(0).getFlight().getTo().getAirportCode());
        Assertions.assertEquals(ticketDtoList.get(0).getDepartureDateTime(), ticketList.get(0).getFlight().getDepartureDateTime());
        Assertions.assertEquals(ticketDtoList.get(0).getArrivalDateTime(), ticketList.get(0).getFlight().getArrivalDateTime());

        Assertions.assertEquals(ticketDtoList.get(0).getFlightSeatId(), ticketList.get(0).getFlightSeat().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getSeatNumber(), ticketList.get(0).getFlightSeat().getSeat().getSeatNumber());
    }

}
