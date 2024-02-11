package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Seat;
import app.entities.Ticket;
import app.enums.Airport;
import app.enums.BookingStatus;
import app.mappers.TicketMapper;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private PassengerService passengerService;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private FlightSeatService flightSeatService;
    @Mock
    private FlightSeatRepository flightSeatRepository;
    @Mock
    private FlightService flightService;
    @Mock
    private BookingService bookingService;
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

    @DisplayName("4 findTicketByTicketNumber (), Positive test finds ticket by ticket number")
    @Test
    public void shouldReturnOneTicketByTicketNumber() {

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

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBooking(booking);
        ticket.setPassenger(passenger);
        ticket.setFlightSeat(flightSeat);

        when(ticketRepository.findByTicketNumberContainingIgnoreCase(ticket.getTicketNumber())).thenReturn(ticket);

        var result = ticketService.getTicketByTicketNumber(ticket.getTicketNumber());

        assertEquals(ticket, result);
    }

    @DisplayName("5 deleteTicketById (), Positive test delete ticket by ticket id")
    @Test
    void shouldDeleteTicketById() {
        ticketService.deleteTicketById(anyLong());
        verify(ticketRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("6 saveTicket (), Positive test save ticket")
    @Test
    void shouldSaveTicket() {

        TicketDto timezoneDto = new TicketDto();
        timezoneDto.setPassengerId(1L);
        timezoneDto.setFlightSeatId(3L);
        timezoneDto.setBookingId(1L);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");
        when(passengerRepository.findByEmail(passenger.getEmail())).thenReturn(passenger);
        when(passengerService.getPassenger(timezoneDto.getPassengerId())).thenReturn(Optional.of(passenger));

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(2L);
        toKoltcovo.setAirportCode(Airport.SVX);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setCode("VKOSVX");
        flight.setFrom(fromVnukovo);
        flight.setTo(toKoltcovo);


        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3L);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);
        flightSeat.setIsSold(false);
        flightSeat.setIsRegistered(false);
        flightSeat.setIsBooked(false);

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        flight.setDepartureDateTime(
                LocalDateTime.of(2023, 4, 1, 1, 0, 0)
        );
        flight.setArrivalDateTime(
                LocalDateTime.of(2023, 4, 1, 2, 0, 0)
        );

        when(flightSeatService.getFlightSeat(timezoneDto.getFlightSeatId())).thenReturn(Optional.of(flightSeat));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        when(bookingService.getBooking(timezoneDto.getBookingId())).thenReturn(Optional.of(booking));

        when(bookingService.getBookingByFlightSeatId(timezoneDto.getFlightSeatId())).thenReturn(Optional.of(booking));

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setPassenger(passenger);
        savedTicket.setBooking(booking);
        savedTicket.setFlightSeat(flightSeat);
        savedTicket.setTicketNumber("LL-4000");
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        when(ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService)).thenReturn(savedTicket);

        Ticket result = ticketService.saveTicket(timezoneDto);

        assertNotNull(result);
        assertNotNull(result.getTicketNumber());
        assertEquals(result.getTicketNumber(),savedTicket.getTicketNumber());
        assertEquals(result.getPassenger(),savedTicket.getPassenger());
        assertEquals(result.getFlightSeat(),savedTicket.getFlightSeat());
        assertEquals(result.getBooking(),savedTicket.getBooking());
    }
}
