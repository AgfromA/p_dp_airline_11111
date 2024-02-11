package app.services;

import app.controllers.rest.BookingRestController;
import app.dto.BookingDto;
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
import app.repositories.BookingRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private BookingRepository bookingRepository;
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

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

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
    @DisplayName("7 createPaidTicket (), Positive test create paid ticket")
    @Test
    void shouldCreatePaidTicket() {
        TicketDto timezoneDto = new TicketDto();
        timezoneDto.setPassengerId(1L);
        timezoneDto.setFlightSeatId(3L);
        timezoneDto.setBookingId(1L);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");

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

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        booking.setId(1L);

        Ticket savedTicket = new Ticket();
        savedTicket.setTicketNumber("LL-4000");
        savedTicket.setPassenger(passenger);
        savedTicket.setFlightSeat(flightSeat);
        savedTicket.setBooking(booking);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        Ticket result = ticketService.createPaidTicket(booking.getId());
        System.out.println(result);
        assertNotNull(result);
        assertEquals(booking.getPassenger(), result.getPassenger());
        assertEquals(booking.getFlightSeat(), result.getFlightSeat());
    }
    @Disabled
    @DisplayName("8 updateTicket (), Positive test update ticket")
    @Test
    public void shouldUpdateTicket() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");

        Passenger passengerUpdated = new Passenger();
        passengerUpdated.setId(2L);
        passengerUpdated.setEmail("promo@example.com");
        passengerUpdated.setLastName("Ермаков");
        passengerUpdated.setFirstName("Степан");

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

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        Flight flightUpdated = new Flight();
        flightUpdated.setId(2L);
        flightUpdated.setCode("VKOSVX");
        flightUpdated.setFrom(fromVnukovo);
        flightUpdated.setTo(toKoltcovo);

        FlightSeat flightSeatUpdated = new FlightSeat();
        flightSeatUpdated.setId(5L);
        flightSeatUpdated.setFlight(flightUpdated);
        flightSeatUpdated.setSeat(seat);

        List<FlightSeat> flightSeats2 = new ArrayList<>();
        flightSeats2.add(flightSeatUpdated);

        flightUpdated.setSeats(flightSeats2);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        booking.setId(1L);

        Booking bookingUpdated = new Booking();
        bookingUpdated.setId(1L);
        bookingUpdated.setBookingStatus(BookingStatus.PAID);
        bookingUpdated.setPassenger(passengerUpdated);
        bookingUpdated.setFlightSeat(flightSeatUpdated);

        Ticket existingTicket = new Ticket();
        existingTicket.setId(208L);
        existingTicket.setBooking(booking);
        existingTicket.setPassenger(passenger);
        existingTicket.setFlightSeat(flightSeat);
        existingTicket.setTicketNumber("HH-4000");

        when(ticketRepository.findTicketById(existingTicket.getId())).thenReturn(existingTicket);

        TicketDto timezoneDto = new TicketDto();
        timezoneDto.setPassengerId(6L);
        timezoneDto.setFlightSeatId(3L);
        timezoneDto.setBookingId(1L);

        Ticket updatedTicket = new Ticket();
        updatedTicket.setBooking(bookingUpdated);
        updatedTicket.setPassenger(passengerUpdated);
        updatedTicket.setFlightSeat(flightSeatUpdated);
        updatedTicket.setTicketNumber(existingTicket.getTicketNumber());

        when(ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService)).thenReturn(updatedTicket);

        when(bookingService.getBookingByFlightSeatId(flightSeat.getId())).thenReturn(Optional.of(bookingUpdated));

        Ticket result = ticketService.updateTicketById(existingTicket.getId(), timezoneDto);

        assertNotNull(result);
    }

    @DisplayName("9 validatePassengerChange (), Positive test existing Ticket has booking and same passenger id")
    @Test
    public void shouldValidatePassengerChange() {

        Ticket existingTicket = new Ticket();
        Booking booking = new Booking();
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        booking.setPassenger(passenger);
        existingTicket.setBooking(booking);
        Long newPassengerId = 1L;

        assertDoesNotThrow(() -> ticketService.validatePassengerChange(existingTicket, newPassengerId));
    }

    @DisplayName("10 validatePassengerChange (), Negative test existing Ticket has booking and different passenger id")
    @Test
    public void shouldValidatePassengerChange2() {
        Ticket existingTicket = new Ticket();
        Booking booking = new Booking();
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        booking.setPassenger(passenger);
        existingTicket.setBooking(booking);
        Long newPassengerId = 2L;

        assertThrows(IllegalArgumentException.class, () -> ticketService.validatePassengerChange(existingTicket, newPassengerId));
    }

    @DisplayName("11 validateFlightSeatChange (), Positive test existing Ticket has booking and same flightSeat id")
    @Test
    public void shouldValidateFlightSeatChange() {
        Ticket existingTicket = new Ticket();
        Booking booking = new Booking();
        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        booking.setFlightSeat(flightSeat);
        existingTicket.setBooking(booking);
        Long newFlightSeatId = 1L;

        assertDoesNotThrow(() -> ticketService.validateFlightSeatChange(existingTicket, newFlightSeatId));
    }
    @DisplayName("11 validateFlightSeatChange (), Negative test existing Ticket has booking and different flightSeat id")
    @Test
    public void shouldValidateFlightSeatChange2() {
        Ticket existingTicket = new Ticket();
        Booking booking = new Booking();
        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        booking.setFlightSeat(flightSeat);
        existingTicket.setBooking(booking);
        Long newFlightSeatId = 2L;

        assertThrows(IllegalArgumentException.class, () -> ticketService.validateFlightSeatChange(existingTicket, newFlightSeatId));
    }
    @DisplayName("12 getFlightSeatIdsByPassengerId (), Positive test return an array of flight seat IDs for a valid passenger ID")
    @Test
    public void shouldReturnFlightSeatIdByPassengerId() {
        long passengerId = 1;
        long[] expected = {1, 2, 3};
        when(ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId)).thenReturn(expected);

        long[] result = ticketService.getFlightSeatIdsByPassengerId(passengerId);

        assertArrayEquals(expected, result);
    }
    @DisplayName("13 getFlightSeatIdsByPassengerId (), Negative test return an empty array for a passenger with no tickets")
    @Test
    public void shouldReturnFlightSeatIdByPassengerId2() {
        long passengerId = 1;
        long[] expected = {};
        when(ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId)).thenReturn(expected);

        long[] result = ticketService.getFlightSeatIdsByPassengerId(passengerId);

        assertArrayEquals(expected, result);
    }

    @DisplayName("14 deleteTicketByPassengerId (), Positive test delete all tickets associated with a passenger ID")
    @Test
    public void ShouldDeleteAllTicketByPassengerId() {
        long passengerId = 1;

        ticketService.deleteTicketByPassengerId(passengerId);

        verify(ticketRepository, times(1)).deleteTicketByPassengerId(passengerId);
    }
    @DisplayName("15 generateUniqueTicketNumberWithCorrectFormat(), Positive test generates a unique ticket number")
    @Test
    public void ShouldGenerateUniqueTicketNumberWithCorrectFormat() {
        String ticketNumber = ticketService.generateTicketNumber();

        assertTrue(ticketNumber.matches("[A-Z]{2}-\\d{4}"));

        String anotherTicketNumber = ticketService.generateTicketNumber();

        assertNotEquals(ticketNumber, anotherTicketNumber);
    }
}
