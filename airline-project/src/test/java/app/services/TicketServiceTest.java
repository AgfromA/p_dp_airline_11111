package app.services;

import app.dto.TicketDto;
import app.entities.Aircraft;
import app.entities.Booking;
import app.entities.Category;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Seat;
import app.entities.Ticket;
import app.enums.BookingStatus;
import app.enums.CategoryType;
import app.mappers.FlightSeatMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.SeatRepository;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private FlightSeatRepository flightSeatRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @DisplayName("1 findAllTickets (), Positive test find 1 ticket")
    @Test
    public void shouldReturnAllTickets() {

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

        doReturn(expectedTickets).when(ticketRepository.findAll());
        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(expectedTickets, actualTickets);
    }
}
