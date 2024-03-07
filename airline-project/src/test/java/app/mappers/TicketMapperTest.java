package app.mappers;

import app.dto.TicketDto;
import app.entities.*;
import app.enums.Airport;
import app.enums.BookingStatus;
import app.services.BookingService;
import app.services.FlightSeatService;
import app.services.FlightService;
import app.services.PassengerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class TicketMapperTest {

    @InjectMocks
    private final TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);
    @Mock
    private PassengerService passengerServiceMock;
    @Mock
    private FlightService flightServiceMock;

    @Mock
    private BookingService bookingServiceMock;

    @Mock
    private FlightSeatService flightSeatServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен корректно конвертировать сущность в ДТО")
    void shouldConvertTicketEntityToTicketDTO() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

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
        flight.setDepartureDateTime(LocalDateTime.of(2024, 2, 12, 12, 00, 00));
        when(flightServiceMock.getFlight(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42L);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);
        when(flightSeatServiceMock.getFlightSeat(3001L)).thenReturn(Optional.of(flightSeat));

        Booking booking = new Booking();
        booking.setId(1111L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        when(bookingServiceMock.getBooking(1111L)).thenReturn(Optional.of(booking));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("OX-2010");
        ticket.setPassenger(passenger);
        ticket.setFlightSeat(flightSeat);
        ticket.setBooking(booking);

        TicketDto ticketDTO = ticketMapper.toDto(ticket);

        Assertions.assertNotNull(ticketDTO);
        Assertions.assertEquals(ticket.getId(), ticketDTO.getId());
        Assertions.assertEquals(ticket.getTicketNumber(), ticketDTO.getTicketNumber());
        Assertions.assertEquals(ticket.getPassenger().getId(), ticketDTO.getPassengerId());
        Assertions.assertEquals(ticket.getPassenger().getFirstName(), ticketDTO.getFirstName());
        Assertions.assertEquals(ticket.getPassenger().getLastName(), ticketDTO.getLastName());

        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getCode(), ticketDTO.getFlightCode());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getFrom().getAirportCode(), ticketDTO.getFrom());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getTo().getAirportCode(), ticketDTO.getTo());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getDepartureDateTime(), ticketDTO.getDepartureDateTime());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getArrivalDateTime(), ticketDTO.getArrivalDateTime());

        Assertions.assertEquals(ticket.getFlightSeat().getId(), ticketDTO.getFlightSeatId());
        Assertions.assertEquals(ticket.getFlightSeat().getSeat().getSeatNumber(), ticketDTO.getSeatNumber());

        Assertions.assertEquals(ticket.getBooking().getId(), ticketDTO.getBookingId());
        Assertions.assertEquals(ticket.getBooking().getPassenger().getId(), ticketDTO.getPassengerId());
        Assertions.assertEquals(ticket.getBooking().getPassenger().getFirstName(), ticketDTO.getFirstName());
        Assertions.assertEquals(ticket.getBooking().getPassenger().getLastName(), ticketDTO.getLastName());
        Assertions.assertEquals(ticket.getBooking().getFlightSeat().getId(), ticketDTO.getFlightSeatId());

        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getDepartureDateTime().minusMinutes(40), ticketDTO.getBoardingStartTime());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getDepartureDateTime().minusMinutes(20), ticketDTO.getBoardingEndTime());
    }

    @Test
    @DisplayName("Должен корректно конвертировать ДТО в сущность")
    void shouldConvertTicketDTOToTicketEntity() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

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
        when(flightServiceMock.getFlight(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42L);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);
        when(flightSeatServiceMock.getFlightSeat(3001L)).thenReturn(Optional.of(flightSeat));

        Booking booking = new Booking();
        booking.setId(1111L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        when(bookingServiceMock.getBooking(1111L)).thenReturn(Optional.of(booking));

        TicketDto ticketDTO = new TicketDto();
        ticketDTO.setId(1L);
        ticketDTO.setTicketNumber("OX-2010");
        ticketDTO.setPassengerId(1001L);
        ticketDTO.setFirstName("Test");
        ticketDTO.setLastName("Testing");

        ticketDTO.setFlightCode("TEST123");
        ticketDTO.setFrom(Airport.ABA);
        ticketDTO.setTo(Airport.AAQ);
        ticketDTO.setDepartureDateTime(null);
        ticketDTO.setArrivalDateTime(null);

        ticketDTO.setFlightSeatId(3001L);
        ticketDTO.setSeatNumber("42L");

        ticketDTO.setBookingId(1111L);

        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(ticketDTO.getId(), ticket.getId());
        Assertions.assertEquals(ticketDTO.getTicketNumber(), ticket.getTicketNumber());
        Assertions.assertEquals(ticketDTO.getPassengerId(), ticket.getPassenger().getId());
        Assertions.assertEquals(ticketDTO.getFirstName(), ticket.getPassenger().getFirstName());
        Assertions.assertEquals(ticketDTO.getLastName(), ticket.getPassenger().getLastName());

        Assertions.assertEquals(ticketDTO.getFlightCode(), ticket.getFlightSeat().getFlight().getCode());
        Assertions.assertEquals(ticketDTO.getFrom(), ticket.getFlightSeat().getFlight().getFrom().getAirportCode());
        Assertions.assertEquals(ticketDTO.getTo(), ticket.getFlightSeat().getFlight().getTo().getAirportCode());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getDepartureDateTime(), ticketDTO.getDepartureDateTime());
        Assertions.assertEquals(ticket.getFlightSeat().getFlight().getArrivalDateTime(), ticketDTO.getArrivalDateTime());

        Assertions.assertEquals(ticketDTO.getFlightSeatId(), ticket.getFlightSeat().getId());
        Assertions.assertEquals(ticketDTO.getSeatNumber(), ticket.getFlightSeat().getSeat().getSeatNumber());

        Assertions.assertEquals(ticketDTO.getBookingId(), ticket.getBooking().getId());

    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию сущностей в ДТО")
    void shouldConvertTicketEntityListToTicketDTOList() throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

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
        flight.setDepartureDateTime(LocalDateTime.of(2024, 2, 12, 12, 00, 00));
        when(flightServiceMock.getFlight(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42L);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);
        when(flightSeatServiceMock.getFlightSeat(3001L)).thenReturn(Optional.of(flightSeat));

        Booking booking = new Booking();
        booking.setId(1111L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        when(bookingServiceMock.getBooking(1111L)).thenReturn(Optional.of(booking));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("OX-2010");
        ticket.setPassenger(passenger);
        ticket.setFlightSeat(flightSeat);
        ticket.setBooking(booking);

        ticketList.add(ticket);

        List<TicketDto> ticketDtoList = ticketMapper.toDtoList(ticketList);

        Assertions.assertEquals(ticketList.size(), ticketDtoList.size());
        Assertions.assertEquals(ticketList.get(0).getId(), ticketDtoList.get(0).getId());
        Assertions.assertEquals(ticketList.get(0).getTicketNumber(), ticketDtoList.get(0).getTicketNumber());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getId(), ticketDtoList.get(0).getPassengerId());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getFirstName(), ticketDtoList.get(0).getFirstName());
        Assertions.assertEquals(ticketList.get(0).getPassenger().getLastName(), ticketDtoList.get(0).getLastName());

        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getCode(), ticketDtoList.get(0).getFlightCode());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getFrom().getAirportCode(), ticketDtoList.get(0).getFrom());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getTo().getAirportCode(), ticketDtoList.get(0).getTo());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getDepartureDateTime(), ticketDtoList.get(0).getDepartureDateTime());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getArrivalDateTime(), ticketDtoList.get(0).getArrivalDateTime());

        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getId(), ticketDtoList.get(0).getFlightSeatId());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getSeat().getSeatNumber(), ticketDtoList.get(0).getSeatNumber());

        Assertions.assertEquals(ticketList.get(0).getBooking().getPassenger().getId(), ticketDtoList.get(0).getPassengerId());
        Assertions.assertEquals(ticketList.get(0).getBooking().getPassenger().getLastName(), ticketDtoList.get(0).getLastName());
        Assertions.assertEquals(ticketList.get(0).getBooking().getPassenger().getFirstName(), ticketDtoList.get(0).getFirstName());
        Assertions.assertEquals(ticketList.get(0).getBooking().getPassenger().getId(), ticketDtoList.get(0).getPassengerId());
        Assertions.assertEquals(ticketList.get(0).getBooking().getFlightSeat().getId(), ticketDtoList.get(0).getFlightSeatId());

        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getDepartureDateTime().minusMinutes(40), ticketDtoList.get(0).getBoardingStartTime());
        Assertions.assertEquals(ticketList.get(0).getFlightSeat().getFlight().getDepartureDateTime().minusMinutes(20), ticketDtoList.get(0).getBoardingEndTime());
    }

    @Test
    @DisplayName("Должен корректно конвертировать коллекцию ДТО в сущности")
    void shouldConvertTicketDTOLisToTicketEntityList() throws Exception {
        List<TicketDto> ticketDtoList = new ArrayList<>();
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        passenger.setFirstName("Test");
        passenger.setLastName("Testing");
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

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
        when(flightServiceMock.getFlight(2001L)).thenReturn(Optional.of(flight));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3001L);
        Seat seat = new Seat();
        seat.setId(42L);
        seat.setSeatNumber("42L");
        flightSeat.setSeat(seat);
        flightSeat.setFlight(flight);
        when(flightSeatServiceMock.getFlightSeat(3001L)).thenReturn(Optional.of(flightSeat));

        Booking booking = new Booking();
        booking.setId(1111L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        when(bookingServiceMock.getBooking(1111L)).thenReturn(Optional.of(booking));

        TicketDto ticketDTO = new TicketDto();
        ticketDTO.setId(1L);
        ticketDTO.setTicketNumber("OX-2010");
        ticketDTO.setPassengerId(1001L);
        ticketDTO.setFirstName("Test");
        ticketDTO.setLastName("Testing");
        ticketDTO.setBookingId(booking.getId());

        ticketDTO.setFlightCode("TEST123");
        ticketDTO.setFrom(Airport.ABA);
        ticketDTO.setTo(Airport.AAQ);
        ticketDTO.setDepartureDateTime(null);
        ticketDTO.setArrivalDateTime(null);

        ticketDTO.setFlightSeatId(3001L);
        ticketDTO.setSeatNumber("42L");

        ticketDtoList.add(ticketDTO);

        List<Ticket> ticketList = ticketMapper.toEntityList(ticketDtoList);

        Assertions.assertEquals(ticketDtoList.size(), ticketList.size());
        Assertions.assertEquals(ticketDtoList.get(0).getId(), ticketList.get(0).getId());
        Assertions.assertEquals(ticketDtoList.get(0).getTicketNumber(), ticketList.get(0).getTicketNumber());
        Assertions.assertEquals(ticketDtoList.get(0).getPassengerId(), ticketList.get(0).getPassenger().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getFirstName(), ticketList.get(0).getPassenger().getFirstName());
        Assertions.assertEquals(ticketDtoList.get(0).getLastName(), ticketList.get(0).getPassenger().getLastName());

        Assertions.assertEquals(ticketDtoList.get(0).getFlightCode(), ticketList.get(0).getFlightSeat().getFlight().getCode());
        Assertions.assertEquals(ticketDtoList.get(0).getFrom(), ticketList.get(0).getFlightSeat().getFlight().getFrom().getAirportCode());
        Assertions.assertEquals(ticketDtoList.get(0).getTo(), ticketList.get(0).getFlightSeat().getFlight().getTo().getAirportCode());
        Assertions.assertEquals(ticketDtoList.get(0).getDepartureDateTime(), ticketList.get(0).getFlightSeat().getFlight().getDepartureDateTime());
        Assertions.assertEquals(ticketDtoList.get(0).getArrivalDateTime(), ticketList.get(0).getFlightSeat().getFlight().getArrivalDateTime());

        Assertions.assertEquals(ticketDtoList.get(0).getFlightSeatId(), ticketList.get(0).getFlightSeat().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getSeatNumber(), ticketList.get(0).getFlightSeat().getSeat().getSeatNumber());

        Assertions.assertEquals(ticketDtoList.get(0).getBookingId(), ticketList.get(0).getBooking().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getPassengerId(), ticketList.get(0).getBooking().getPassenger().getId());
        Assertions.assertEquals(ticketDtoList.get(0).getLastName(), ticketList.get(0).getBooking().getPassenger().getLastName());
        Assertions.assertEquals(ticketDtoList.get(0).getFirstName(), ticketList.get(0).getBooking().getPassenger().getFirstName());
        Assertions.assertEquals(ticketDtoList.get(0).getFlightSeatId(), ticketList.get(0).getBooking().getFlightSeat().getId());
    }
}
