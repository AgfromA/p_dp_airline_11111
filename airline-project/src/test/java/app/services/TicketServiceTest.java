package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Seat;
import app.entities.Ticket;
import app.enums.BookingStatus;
import app.mappers.TicketMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private FlightSeatRepository flightSeatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @DisplayName("1 findAllTickets (), Positive test finds 1 ticket")
    @Test
    public void shouldReturnOneTicket() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBookingStatus(BookingStatus.PAID);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBookingId(booking.getId());
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
         expectedTickets.add(ticket);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(expectedTickets);

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(1, actualTickets.size());
    }

    @DisplayName("2 findAllTickets (), Positive test finds 2 tickets")
    @Test
    public void shouldReturnTwoTickets() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBookingStatus(BookingStatus.PAID);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBookingId(booking.getId());
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Лиза");
        passenger2.setLastName("Теплицына");

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setPassenger(passenger2);
        booking2.setBookingStatus(BookingStatus.PAID);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("2A");

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(2L);
        flightSeat2.setSeat(seat2);

        TicketDto ticket2 = new TicketDto();
        ticket2.setId(2L);
        ticket2.setTicketNumber("LL-4001");
        ticket2.setBookingId(booking2.getId());
        ticket2.setPassengerId(passenger2.getId());
        ticket2.setFlightSeatId(flightSeat2.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
        expectedTickets.add(ticket);
        expectedTickets.add(ticket2);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(expectedTickets);

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(2, actualTickets.size());
    }
    @DisplayName("3 findAllTickets (), Negative test finds 2 tickets")
    @Test
    public void shouldReturnAllTickets() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Лиза");
        passenger2.setLastName("Теплицына");

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("2A");

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(2L);
        flightSeat2.setSeat(seat2);

        TicketDto ticket2 = new TicketDto();
        ticket2.setId(2L);
        ticket2.setTicketNumber("LL-4001");
        ticket2.setPassengerId(passenger2.getId());
        ticket2.setFlightSeatId(flightSeat2.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
        expectedTickets.add(ticket);
        expectedTickets.add(ticket2);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(new ArrayList<>());

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(0, actualTickets.size());
    }
}
